import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.java.decompiler.main.extern.IIdentifierRenamer;

/**
 * Vineflower identifier renamer driven by a plain-text map file (path in the
 * system property ch.map). See clonehome/docs/names.map for the format.
 *
 * Renaming happens on the class files' own symbol tables, so every reference
 * (including inherited members reached through a subclass) is rewritten
 * consistently -- something regexes over single-letter identifiers cannot do.
 */
public class ChRenamer implements IIdentifierRenamer {
    private final Map<String, String> classes = new HashMap<>();
    private final Map<String, String> fields = new HashMap<>();
    private final Map<String, String> methods = new HashMap<>();
    // Vineflower re-asks until the name is unique in its scope; answering the same name
    // forever would hang it, so a repeated request gets a numeric suffix (and is reported).
    private final Map<String, Integer> asked = new HashMap<>();

    private String unique(String key, String name) {
        int n = asked.merge(key, 1, Integer::sum);
        if (n == 1) return name;
        System.err.println("ch_rename: name clash for " + key + " -> " + name + " (retry " + n + ")");
        return name + "_" + n;
    }

    public ChRenamer() {
        String path = System.getProperty("ch.map");
        if (path == null) throw new IllegalStateException("-Dch.map=<file> not set");
        try {
            for (String raw : Files.readAllLines(Path.of(path), StandardCharsets.UTF_8)) {
                String line = raw.strip();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] p = line.split("\\s+");
                if (p.length < 3) throw new IllegalStateException("bad map line: " + raw);
                switch (p[0]) {
                    case "class" -> classes.put(p[1], p[2]);
                    case "field" -> fields.put(p[1], p[2]);
                    case "method" -> methods.put(p[1], p[2]);
                    default -> throw new IllegalStateException("bad map line: " + raw);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean toBeRenamed(Type type, String className, String element, String descriptor) {
        if (System.getProperty("ch.debug") != null) System.err.println("toBeRenamed " + type + " " + className + " " + element + " " + descriptor);
        return switch (type) {
            case ELEMENT_CLASS -> classes.containsKey(className);
            case ELEMENT_FIELD -> fields.containsKey(className + "." + element);
            case ELEMENT_METHOD -> methods.containsKey(className + "." + element + params(descriptor));
        };
    }

    @Override
    public String getNextClassName(String fullName, String shortName) {
        String n = classes.get(fullName);
        return n != null ? unique("class " + fullName, n) : fullName;
    }

    @Override
    public String getNextFieldName(String className, String field, String descriptor) {
        String k = className + "." + field;
        String n = fields.get(k);
        return n != null ? unique("field " + k, n) : field;
    }

    @Override
    public String getNextMethodName(String className, String method, String descriptor) {
        String k = className + "." + method + params(descriptor);
        String n = methods.get(k);
        return n != null ? unique("method " + k, n) : method;
    }

    /** "(ILjava/lang/String;[B)V" -> "(int,String,byte[])". */
    static String params(String desc) {
        StringBuilder out = new StringBuilder("(");
        int i = 1;
        boolean first = true;
        while (desc.charAt(i) != ')') {
            int dims = 0;
            while (desc.charAt(i) == '[') { dims++; i++; }
            String t;
            char c = desc.charAt(i);
            if (c == 'L') {
                int end = desc.indexOf(';', i);
                String full = desc.substring(i + 1, end);
                t = full.substring(full.lastIndexOf('/') + 1);
                i = end + 1;
            } else {
                t = switch (c) {
                    case 'I' -> "int";
                    case 'Z' -> "boolean";
                    case 'B' -> "byte";
                    case 'C' -> "char";
                    case 'S' -> "short";
                    case 'J' -> "long";
                    case 'F' -> "float";
                    case 'D' -> "double";
                    default -> throw new IllegalArgumentException(desc);
                };
                i++;
            }
            if (!first) out.append(',');
            first = false;
            out.append(t);
            for (int d = 0; d < dims; d++) out.append("[]");
        }
        return out.append(')').toString();
    }
}
