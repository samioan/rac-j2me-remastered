#!/usr/bin/env python3
"""Translate the renamed Clone Home Java (clonehome/src/) to C++.

The decompiled game is a small, regular Java subset (no generics, lambdas,
floats or finally blocks), so a direct translation is faithful and can be
re-run whenever names.map changes:

    python tools/rename_ch.py        # regenerate clonehome/src
    python tools/java2cpp.py         # regenerate clonehome/port/src/gen

Design: every Java class reference becomes a raw pointer, arrays become
`Arr<T>` (nullable, bounds-checked, shared), `String` is an immutable UTF-16
value type (see port/src/jrt.h). A light type inference over the AST decides
`->` versus `.`, string concatenation and `>>>`. Statements whose evaluation
order could differ between Java (strict left to right) and C++ are reported
so they can be checked by hand.

Requires `pip install javalang`. Hand-written: Engine, Sprite (and the runtime in port/src).
"""
import re
import sys
from pathlib import Path

import javalang
from javalang import tree as T

sys.path.insert(0, str(Path(__file__).resolve().parent))
import ch_widescreen  # noqa: E402

def _patch_javalang():
    """javalang drops a prefix operator in front of a cast (`-(int)x`); keep it."""
    from javalang import parser as P, tree as TT
    from javalang.tokenizer import Operator

    def parse_expression_3(self):
        prefix_operators = []
        while self.tokens.look().value in Operator.PREFIX:
            prefix_operators.append(self.tokens.next().value)
        if self.would_accept("("):
            try:
                with self.tokens:
                    self.accept("(")
                    cast_target = self.parse_type()
                    self.accept(")")
                    expression = self.parse_expression_3()
                    cast = TT.Cast(type=cast_target, expression=expression)
                    cast.prefix_operators = prefix_operators
                    return cast
            except P.JavaSyntaxError:
                pass
        primary = self.parse_primary()
        primary.prefix_operators = prefix_operators
        primary.selectors = []
        primary.postfix_operators = []
        token = self.tokens.look()
        while token.value in "[.":
            selector = self.parse_selector()
            selector._position = token.position
            primary.selectors.append(selector)
            token = self.tokens.look()
        while token.value in Operator.POSTFIX:
            primary.postfix_operators.append(self.tokens.next().value)
            token = self.tokens.look()
        return primary

    P.Parser.parse_expression_3 = parse_expression_3


_patch_javalang()

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "clonehome" / "src"
OUT = ROOT / "clonehome" / "port" / "src" / "gen"

TRANSLATE = ["Game", "Enemy", "Projectile", "RatchetMIDlet", "SoundPlayer"]
ALL_CLASSES = ["Game", "Enemy", "Projectile", "RatchetMIDlet", "Engine", "Sprite", "SoundPlayer"]
# Library/platform classes that are references (pointers) on the C++ side.
LIB_PTR = {"Graphics": "Surface", "Image": "Image", "DataInputStream": "DataInputStream",
           "Random": "Random", "StringBuffer": "StringBuffer", "MIDlet": "MIDlet", "Display": "Display",
           "Font": "Font", "DirectGraphics": "DirectGraphics", "Player": "Player"}
VIRTUALS = {"render", "update", "onLifecycle"}  # Engine methods Game overrides
RESERVED = {"and", "or", "not", "xor", "bool", "template", "typename", "namespace", "union", "auto", "register",
            "operator", "delete", "friend", "inline", "mutable", "explicit", "export", "typedef", "sizeof",
            "signed", "unsigned", "struct", "virtual", "wchar_t", "NULL", "near", "far", "small", "interface",
            "min", "max", "IN", "OUT", "and_eq", "or_eq", "xor_eq", "not_eq", "bitand", "bitor", "compl",
            "asm", "concept", "requires", "constexpr", "decltype", "nullptr", "static_assert", "thread_local",
            "alignas", "alignof", "noexcept", "char16_t", "char32_t", "int8_t", "int16_t", "int64_t", "String",
            "Arr", "small", "byte", "delete", "new", "this", "true", "false", "case", "extern", "goto", "long",
            "short", "float", "double", "void", "int", "char", "if", "else", "do", "while", "for", "switch",
            "default", "break", "continue", "return", "try", "catch", "throw", "static", "const", "volatile",
            "enum", "class", "public", "private", "protected", "using", "GetObject", "ERROR", "TRUE", "FALSE",
            "CONST", "VOID", "HANDLE", "BOOL", "FAR", "NEAR", "DWORD", "WORD", "BYTE", "CALLBACK", "PASCAL"}

PRIM = {"int": "int", "byte": "int8_t", "short": "int16_t", "long": "int64_t", "char": "char16_t",
        "boolean": "bool", "void": "void"}
NUMERIC = {"byte", "short", "char", "int", "long"}


def cname(n):
    return n + "_" if n in RESERVED else n


# A type is (base, dims). base: primitive name, "String", class name, "null", "?"
def jtype(node):
    if node is None:
        return ("void", 0)
    dims = len(node.dimensions) if getattr(node, "dimensions", None) else 0
    return (node.name, dims)


def cpp_type(t):
    base, dims = t
    if dims:
        return "Arr<%s>" % cpp_type((base, dims - 1))
    if base in PRIM:
        return PRIM[base]
    if base == "String":
        return "String"
    if base in ALL_CLASSES:
        return base + "*"
    if base in LIB_PTR:
        return LIB_PTR[base] + "*"
    if base == "?":
        return "auto"
    return base + "*"


def default_init(t):
    base, dims = t
    if dims == 0 and base in PRIM:
        return "false" if base == "boolean" else "0"
    if dims == 0 and base != "String":
        return "nullptr"  # Java fields default to null; C++ pointers would be garbage
    return None


def promote(a, b=None):
    def p(x):
        return "long" if x == "long" else "int"
    if b is None:
        return (p(a[0]), 0)
    return ("long" if "long" in (a[0], b[0]) else "int", 0)


class Sym:
    def __init__(self):
        self.fields = {}   # name -> (type, static)
        self.methods = {}  # name -> [(param types, ret, static)]
        self.super = None


SYMS = {}


def load_symbols():
    for name in ALL_CLASSES:
        tr = javalang.parse.parse(ch_widescreen.apply(name + ".java", (SRC / (name + ".java")).read_text(encoding="utf-8")))
        cls = tr.types[0]
        s = Sym()
        s.super = cls.extends.name if cls.extends else None
        for m in cls.body:
            if isinstance(m, T.FieldDeclaration):
                for d in m.declarators:
                    t = jtype(m.type)
                    t = (t[0], t[1] + len(d.dimensions or []))
                    s.fields[d.name] = (t, "static" in m.modifiers)
            elif isinstance(m, T.MethodDeclaration):
                s.methods.setdefault(m.name, []).append(
                    ([jtype(p.type) for p in m.parameters], jtype(m.return_type), "static" in m.modifiers))
        SYMS[name] = s
        SYMS[name + ".tree"] = tr


def find_field(cls, name):
    while cls in SYMS:
        s = SYMS[cls]
        if name in s.fields:
            return cls, s.fields[name]
        cls = s.super
    return None


def find_methods(cls, name):
    while cls in SYMS:
        s = SYMS[cls]
        if name in s.methods:
            return cls, s.methods[name]
        cls = s.super
    return None


LIB_METHODS = {
    ("String", "length"): ("int", 0), ("String", "charAt"): ("char", 0), ("String", "equals"): ("boolean", 0),
    ("String", "substring"): ("String", 0), ("String", "indexOf"): ("int", 0), ("String", "trim"): ("String", 0),
    ("String", "toCharArray"): ("char", 1), ("String", "getChars"): ("void", 0),
    ("Graphics", "getClipX"): ("int", 0), ("Graphics", "getClipY"): ("int", 0),
    ("Graphics", "getClipWidth"): ("int", 0), ("Graphics", "getClipHeight"): ("int", 0),
    ("Image", "getWidth"): ("int", 0), ("Image", "getHeight"): ("int", 0),
    ("DataInputStream", "readByte"): ("byte", 0), ("DataInputStream", "readShort"): ("short", 0),
    ("DataInputStream", "readChar"): ("char", 0), ("DataInputStream", "readInt"): ("int", 0),
    ("DataInputStream", "readUnsignedByte"): ("int", 0), ("DataInputStream", "read"): ("int", 0),
    ("DataInputStream", "skip"): ("long", 0), ("DataInputStream", "readBoolean"): ("boolean", 0),
    ("DataInputStream", "available"): ("int", 0),
    ("Random", "nextInt"): ("int", 0), ("Random", "nextBoolean"): ("boolean", 0),
    ("StringBuffer", "append"): ("StringBuffer", 0), ("StringBuffer", "toString"): ("String", 0),
    ("StringBuffer", "length"): ("int", 0),
}
STATIC_LIB = {
    ("Math", "abs"), ("Math", "max"), ("Math", "min"), ("System", "currentTimeMillis"), ("System", "arraycopy"),
    ("System", "gc"), ("String", "valueOf"), ("Character", "isDigit"), ("Character", "digit"),
    ("Display", "getDisplay"), ("Image", "createImage"), ("DirectUtils", "getDirectGraphics"),
}


class Ctx:
    def __init__(self, cls, static, ret):
        self.cls, self.static, self.ret = cls, static, ret
        self.scopes = [{}]
        self.hoist = []      # declarations hoisted out of a switch

    def push(self):
        self.scopes.append({})

    def pop(self):
        self.scopes.pop()

    def declare(self, name, t):
        self.scopes[-1][name] = t

    def local(self, name):
        for s in reversed(self.scopes):
            if name in s:
                return s[name]
        return None


WARN = []
LIBCALLS = set()


class Emit:
    def __init__(self):
        self.ctx = None
        self.cur_stmt = ""

    # ---------------------------------------------------------- resolution
    def resolve_chain(self, parts, first_is_call=False):
        """Resolve dotted qualifier parts. Returns (code, type|('class',name))."""
        ctx = self.ctx
        p0 = parts[0]
        loc = ctx.local(p0)
        if loc is not None:
            code, t = cname(p0), loc
        else:
            f = find_field(ctx.cls, p0)
            if f is not None:
                code, t = cname(p0), f[1][0]
            elif p0 in ALL_CLASSES or p0 in LIB_PTR or p0 in ("Math", "System", "String", "Character", "DirectUtils"):
                code, t = p0, ("class", p0)
            elif p0[:1].isupper():
                code, t = p0, ("class", p0)
            else:
                code, t = cname(p0), ("?", 0)
        for part in parts[1:]:
            code, t = self.member(code, t, part)
        return code, t

    def member(self, code, t, name):
        if t[0] == "class":
            f = find_field(t[1], name)
            return "%s::%s" % (t[1], cname(name)), (f[1][0] if f else ("?", 0))
        if t[1] > 0 and name == "length":
            return "%s.length()" % code, ("int", 0)
        f = find_field(t[0], name) if t[0] in SYMS else None
        ft = f[1][0] if f else ("?", 0)
        if f and f[1][1] and False:
            pass
        return "%s->%s" % (code, cname(name)), ft

    # --------------------------------------------------------- expressions
    def expr(self, n):
        code, t = self.expr_base(n)
        if isinstance(n, (T.BinaryOperation, T.TernaryExpression, T.Assignment)):
            code, t = self.selectors(n, code, t)
            code, t = self.apply_ops(n, code, t)
        return code, t

    def apply_ops(self, n, code, t):
        for op in getattr(n, "postfix_operators", None) or []:
            code = "(%s%s)" % (code, op)
        for op in reversed(getattr(n, "prefix_operators", None) or []):
            if op in ("++", "--"):
                code = "(%s%s)" % (op, code)
            elif op == "!":
                code, t = "(!%s)" % code, ("boolean", 0)
            else:
                if op == "-" and re.match(r"^\d+$", code):
                    code = "(-%s)" % code
                else:
                    code = "(%s%s)" % (op, code)
                t = promote(t)
        return code, t

    def selectors(self, n, code, t):
        for s in getattr(n, "selectors", None) or []:
            if isinstance(s, T.ArraySelector):
                ic, _ = self.expr(s.index)
                code, t = "%s[%s]" % (code, ic), (t[0], max(t[1] - 1, 0))
            elif isinstance(s, T.MemberReference):
                code, t = self.member(code, t, s.member)
            elif isinstance(s, T.MethodInvocation):
                code, t = self.call_on(code, t, s.member, s.arguments)
            else:
                raise NotImplementedError(type(s).__name__)
        return code, t

    def args(self, arguments):
        cs, ts = [], []
        for a in arguments:
            c, t = self.expr(a)
            cs.append(c)
            ts.append(t)
        return cs, ts

    def call_on(self, code, t, name, arguments):
        cs, ts = self.args(arguments)
        if t[0] == "class":
            cls = t[1]
            key = (cls, name)
            if cls == "Math":
                rt = ts[0] if len(ts) == 1 else promote(ts[0], ts[1])
                rt = promote(rt)
                cast = "int64_t" if rt[0] == "long" else "int"
                return "jlang::%s<%s>(%s)" % (name, cast, ", ".join("(%s)(%s)" % (cast, c) for c in cs)), rt
            if cls == "System":
                fn = {"currentTimeMillis": "jlang::currentTimeMillis", "arraycopy": "arraycopy",
                      "gc": "jlang::gc"}[name]
                return "%s(%s)" % (fn, ", ".join(cs)), (("long", 0) if name == "currentTimeMillis" else ("void", 0))
            if cls == "Character":
                return "jlang::%s(%s)" % (name, ", ".join(cs)), (("boolean", 0) if name == "isDigit" else ("int", 0))
            if cls == "String" and name == "valueOf":
                a = cs[0]
                if ts[0][1] == 0 and ts[0][0] in ("byte", "short"):
                    a = "(int)(%s)" % a
                return "String::valueOf(%s)" % a, ("String", 0)
            if cls in SYMS:
                m = find_methods(cls, name)
                rt = m[1][0][1] if m else ("?", 0)
                return "%s::%s(%s)" % (cls, cname(name), ", ".join(cs)), rt
            return "%s::%s(%s)" % (cls, name, ", ".join(cs)), ("?", 0)
        if t[1] > 0:
            return "%s.%s(%s)" % (code, name, ", ".join(cs)), ("?", 0)
        base = t[0]
        if base == "String":
            rt = LIB_METHODS.get(("String", name), ("?", 0))
            return "%s.%s(%s)" % (code, name, ", ".join(cs)), rt
        if base in SYMS:
            m = find_methods(base, name)
            rt = m[1][0][1] if m else ("?", 0)
            return "%s->%s(%s)" % (code, cname(name), ", ".join(cs)), rt
        rt = LIB_METHODS.get((base, name), ("?", 0))
        LIBCALLS.add((base, name, len(cs)))
        return "%s->%s(%s)" % (code, name, ", ".join(cs)), rt

    def literal(self, v):
        if v == "null":
            return "nullptr", ("null", 0)
        if v in ("true", "false"):
            return v, ("boolean", 0)
        if v.startswith('"'):
            return 'String(u%s)' % v, ("String", 0)
        if v.startswith("'"):
            body = v[1:-1]
            simple = {"n": 10, "t": 9, "r": 13, "b": 8, "f": 12, "0": 0, "'": 39, '"': 34, "\\": 92}
            if body.startswith("\\u"):
                val = int(body[2:], 16)
            elif body.startswith("\\") and body[1:] in simple:
                val = simple[body[1:]]
            elif body.startswith("\\"):
                val = int(body[1:], 8)
            else:
                val = ord(body)
            return "((char16_t)%d)" % val, ("char", 0)
        if v[-1] in "lL":
            return v[:-1] + "LL", ("long", 0)
        if v.lower().startswith("0x"):
            if int(v, 16) > 0x7FFFFFFF:
                return "(int)%su" % v, ("int", 0)
            return v, ("int", 0)
        return v, ("int", 0)

    def expr_base(self, n):
        if isinstance(n, T.Literal):
            c, t = self.literal(n.value)
            return self.apply_ops(n, c, t)
        if isinstance(n, T.This):
            c, t = "this", (self.ctx.cls, 0)
            c, t = self.selectors(n, c, t)
            return self.apply_ops(n, c, t)
        if isinstance(n, T.MemberReference):
            parts = (n.qualifier.split(".") if n.qualifier else []) + [n.member]
            c, t = self.resolve_chain(parts)
            c, t = self.selectors(n, c, t)
            return self.apply_ops(n, c, t)
        if isinstance(n, T.MethodInvocation):
            if n.qualifier:
                qc, qt = self.resolve_chain(n.qualifier.split("."))
                c, t = self.call_on(qc, qt, n.member, n.arguments)
            else:
                cs, ts = self.args(n.arguments)
                m = find_methods(self.ctx.cls, n.member)
                t = m[1][0][1] if m else ("?", 0)
                c = "%s(%s)" % (cname(n.member), ", ".join(cs))
            c, t = self.selectors(n, c, t)
            return self.apply_ops(n, c, t)
        if isinstance(n, T.SuperMethodInvocation):
            cs, _ = self.args(n.arguments)
            sup = SYMS[self.ctx.cls].super
            m = find_methods(sup, n.member)
            return "%s::%s(%s)" % (sup, cname(n.member), ", ".join(cs)), (m[1][0][1] if m else ("?", 0))
        if isinstance(n, T.SuperMemberReference):
            return "%s::%s" % (SYMS[self.ctx.cls].super, cname(n.member)), ("?", 0)
        if isinstance(n, T.ClassCreator):
            cs, ts = self.args(n.arguments)
            base = n.type.name
            if base == "String":
                c, t = "String(%s)" % ", ".join(cs), ("String", 0)
            else:
                c, t = "new %s(%s)" % (base, ", ".join(cs)), (base, 0)
            c, t = self.selectors(n, c, t)
            return self.apply_ops(n, c, t)
        if isinstance(n, T.ArrayCreator):
            return self.array_creator(n)
        if isinstance(n, T.Cast):
            c, t = self.expr(n.expression)
            ct = jtype(n.type)
            return self.apply_ops(n, "((%s)(%s))" % (cpp_type(ct), c), ct)
        if isinstance(n, T.BinaryOperation):
            return self.binary(n)
        if isinstance(n, T.TernaryExpression):
            cc, _ = self.expr(n.condition)
            ac, at = self.expr(n.if_true)
            bc, bt = self.expr(n.if_false)
            if at[0] in NUMERIC and bt[0] in NUMERIC and at[1] == 0 and bt[1] == 0 and at != bt:
                t = promote(at, bt)
                ac, bc = "(%s)(%s)" % (cpp_type(t), ac), "(%s)(%s)" % (cpp_type(t), bc)
            else:
                t = bt if at[0] == "null" else at
                if at[0] == "null" and bt[0] != "null":
                    ac = "(%s)nullptr" % cpp_type(bt)
                elif bt[0] == "null" and at[0] != "null":
                    bc = "(%s)nullptr" % cpp_type(at)
            return "(%s ? %s : %s)" % (cc, ac, bc), t
        if isinstance(n, T.Assignment):
            return self.assignment(n)
        if isinstance(n, T.ArrayInitializer):
            raise NotImplementedError("bare ArrayInitializer needs a declared type")
        raise NotImplementedError(type(n).__name__)

    def array_creator(self, n):
        base = n.type.name
        total = len(n.dimensions)
        if n.initializer is not None:
            t = (base, total)
            c = self.initializer(n.initializer, t)
            return c, t
        dims = [d for d in n.dimensions if d is not None]
        t = (base, total)
        codes = [self.expr(d)[0] for d in dims]
        if len(dims) == 1:
            return "newArr<%s>(%s)" % (cpp_type((base, total - 1)), codes[0]), t
        if len(dims) == 2 and total == 2:
            return "newArr2<%s>(%s, %s)" % (cpp_type((base, 0)), codes[0], codes[1]), t
        raise NotImplementedError("array creator %r" % (n,))

    def initializer(self, init, t):
        elem = (t[0], t[1] - 1)
        items = []
        for e in init.initializers:
            if isinstance(e, T.ArrayInitializer):
                items.append(self.initializer(e, elem))
            else:
                c, et = self.expr(e)
                items.append(c)
        return "%s::make(%s)" % (cpp_type(t), ", ".join(items))

    def binary(self, n):
        op = n.operator
        lc, lt = self.expr(n.operandl)
        rc, rt = self.expr(n.operandr)
        if op == "+" and (lt[0] == "String" and lt[1] == 0 or rt[0] == "String" and rt[1] == 0):
            def s(c, t):
                if t[1] == 0 and t[0] in ("byte", "short"):
                    c = "(int)(%s)" % c
                return "jstr(%s)" % c
            return "(%s + %s)" % (s(lc, lt), s(rc, rt)), ("String", 0)
        if op in ("==", "!=", "<", ">", "<=", ">=", "&&", "||"):
            return "(%s %s %s)" % (lc, op, rc), ("boolean", 0)
        if op == ">>>":
            lt2 = promote(lt)
            if lt2[0] == "long":
                return "((int64_t)((uint64_t)(%s) >> (%s)))" % (lc, rc), lt2
            return "((int)((uint32_t)(%s) >> (%s)))" % (lc, rc), lt2
        if op in ("<<", ">>"):
            return "(%s %s %s)" % (lc, op, rc), promote(lt)
        if op in ("&", "|", "^") and lt[0] == "boolean":
            return "(%s %s %s)" % (lc, op, rc), ("boolean", 0)
        return "(%s %s %s)" % (lc, op, rc), promote(lt, rt)

    def assignment(self, n):
        lc, lt = self.expr(n.expressionl)
        if isinstance(n.value, T.ArrayInitializer):
            rc, rt = self.initializer(n.value, lt), lt
        else:
            rc, rt = self.expr(n.value)
        op = n.type
        if op == ">>>=":
            if lt[0] == "long":
                return "(%s = (int64_t)((uint64_t)(%s) >> (%s)))" % (lc, lc, rc), lt
            return "(%s = (%s)((int)((uint32_t)(%s) >> (%s))))" % (lc, cpp_type(lt), lc, rc), lt
        if op == "+=" and lt[0] == "String" and lt[1] == 0:
            return "(%s = %s + jstr(%s))" % (lc, lc, rc), lt
        if op == "=" and rt[0] == "null" and lt[0] == "String" and lt[1] == 0:
            rc = "String(nullptr)"
        return "(%s %s %s)" % (lc, op, rc), lt

    # ------------------------------------------------------------ warnings
    def order_check(self, stmt_src, node):
        """Flag statements where a variable is modified by ++/--/nested
        assignment and mentioned again in the same statement."""
        writes, names = [], []

        def walk(x, top):
            if isinstance(x, T.Assignment):
                if not top:
                    writes.append(x.expressionl)
                walk(x.expressionl, False)
                walk(x.value, False)
                return
            if isinstance(x, javalang.ast.Node):
                for op in (getattr(x, "prefix_operators", None) or []) + (getattr(x, "postfix_operators", None) or []):
                    if op in ("++", "--"):
                        writes.append(x)
                for c in x.children:
                    if isinstance(c, (javalang.ast.Node, list)):
                        walk(c, False)
            elif isinstance(x, list):
                for c in x:
                    walk(c, False)

        def key(x):
            if isinstance(x, T.MemberReference):
                return (x.qualifier or "") + "." + x.member
            if isinstance(x, T.This):
                return "this." + ".".join(s.member for s in x.selectors if isinstance(s, T.MemberReference))
            return None

        def collect(x):
            if isinstance(x, javalang.ast.Node):
                k = key(x)
                if k:
                    names.append(k)
                for c in x.children:
                    if isinstance(c, (javalang.ast.Node, list)):
                        collect(c)
            elif isinstance(x, list):
                for c in x:
                    collect(c)

        walk(node, True)
        collect(node)
        for w in writes:
            k = key(w)
            if k and names.count(k) > 1:
                WARN.append((k, stmt_src))
                return True
        return False

    # ---------------------------------------------------------- statements
    def block(self, stmts, indent):
        return "".join(self.stmt(s, indent) for s in stmts)

    def body(self, s, indent):
        if isinstance(s, T.BlockStatement):
            self.ctx.push()
            r = self.block(s.statements, indent)
            self.ctx.pop()
            return r
        if s is None:
            return ""
        self.ctx.push()
        r = self.stmt(s, indent)
        self.ctx.pop()
        return r

    def decl(self, n, indent, in_switch=False):
        t0 = jtype(n.type)
        out = []
        for d in n.declarators:
            t = (t0[0], t0[1] + len(d.dimensions or []))
            self.ctx.declare(d.name, t)
            name = cname(d.name)
            if in_switch:
                self.ctx.hoist.append("%s %s;" % (cpp_type(t), name))
                if d.initializer is not None:
                    out.append("%s%s = %s;\n" % (indent, name, self.init_value(d.initializer, t)))
            else:
                if d.initializer is not None:
                    out.append("%s%s %s = %s;\n" % (indent, cpp_type(t), name, self.init_value(d.initializer, t)))
                else:
                    di = default_init(t)
                    out.append("%s%s %s%s;\n" % (indent, cpp_type(t), name, " = " + di if di else ""))
        return "".join(out)

    def init_value(self, e, t):
        if isinstance(e, T.ArrayInitializer):
            return self.initializer(e, t)
        c, et = self.expr(e)
        self.order_check(c, e)
        if et[0] == "null" and t[0] == "String" and t[1] == 0:
            return "String(nullptr)"
        return c

    def stmt(self, s, ind):
        I = ind
        if isinstance(s, T.StatementExpression):
            c, _ = self.expr(s.expression)
            if c.startswith("(") and c.endswith(")") and self._balanced(c[1:-1]):
                c = c[1:-1]
            flag = "  // CHECK-ORDER" if self.order_check(c, s.expression) else ""
            return "%s%s;%s\n" % (I, c, flag)
        if isinstance(s, T.LocalVariableDeclaration):
            return self.decl(s, I, in_switch=self.in_switch)
        if isinstance(s, T.BlockStatement):
            r = "%s{\n%s%s}\n" % (I, self.body(s, I + "  "), I)
            if getattr(s, "label", None):
                r += "%s%s_end:;\n" % (I, s.label)
            return r
        if isinstance(s, T.IfStatement):
            cc, _ = self.expr(s.condition)
            self.order_check(cc, s.condition)
            r = "%sif (%s) {\n%s%s}" % (I, cc, self.body(s.then_statement, I + "  "), I)
            e = s.else_statement
            while e is not None:
                if isinstance(e, T.IfStatement):
                    ec, _ = self.expr(e.condition)
                    r += " else if (%s) {\n%s%s}" % (ec, self.body(e.then_statement, I + "  "), I)
                    e = e.else_statement
                else:
                    r += " else {\n%s%s}" % (self.body(e, I + "  "), I)
                    e = None
            return r + "\n"
        if isinstance(s, T.WhileStatement):
            cc, _ = self.expr(s.condition)
            self.order_check(cc, s.condition)
            return "%swhile (%s) {\n%s%s}\n" % (I, cc, self.body(s.body, I + "  "), I)
        if isinstance(s, T.DoStatement):
            cc, _ = self.expr(s.condition)
            self.order_check(cc, s.condition)
            return "%sdo {\n%s%s} while (%s);\n" % (I, self.body(s.body, I + "  "), I, cc)
        if isinstance(s, T.ForStatement):
            return self.for_stmt(s, I)
        if isinstance(s, T.SwitchStatement):
            return self.switch(s, I)
        if isinstance(s, T.ReturnStatement):
            if s.expression is None:
                return "%sreturn;\n" % I
            c, t = self.expr(s.expression)
            self.order_check(c, s.expression)
            if t[0] == "null" and self.ctx.ret[0] == "String" and self.ctx.ret[1] == 0:
                c = "String(nullptr)"
            return "%sreturn %s;\n" % (I, c)
        if isinstance(s, T.BreakStatement):
            if s.goto:
                return "%sgoto %s_end;\n" % (I, s.goto)
            return "%sbreak;\n" % I
        if isinstance(s, T.ContinueStatement):
            return "%scontinue;\n" % I
        if isinstance(s, T.TryStatement):
            r = "%stry {\n%s%s}" % (I, self.body_list(s.block, I + "  "), I)
            r += " catch (...) {\n%s%s}\n" % (self.body_list(s.catches[0].block, I + "  "), I)
            return r
        if isinstance(s, T.ThrowStatement):
            return "%sthrow JavaException{\"throw\"};\n" % I
        if isinstance(s, T.SynchronizedStatement):
            return "%s{\n%s%s}\n" % (I, self.body_list(s.block, I + "  "), I)
        if s is None or (isinstance(s, T.Statement) and type(s) is T.Statement):
            return "%s;\n" % I
        raise NotImplementedError(type(s).__name__)

    in_switch = False

    def body_list(self, stmts, indent):
        self.ctx.push()
        r = self.block(stmts, indent)
        self.ctx.pop()
        return r

    @staticmethod
    def _balanced(s):
        d = 0
        for ch in s:
            if ch == "(":
                d += 1
            elif ch == ")":
                d -= 1
                if d < 0:
                    return False
        return d == 0

    def for_stmt(self, s, I):
        c = s.control
        self.ctx.push()
        if isinstance(c, T.EnhancedForControl):
            raise NotImplementedError("enhanced for")
        init = ""
        if c.init is not None:
            if isinstance(c.init, T.VariableDeclaration):
                t0 = jtype(c.init.type)
                parts = []
                for d in c.init.declarators:
                    t = (t0[0], t0[1] + len(d.dimensions or []))
                    self.ctx.declare(d.name, t)
                    parts.append("%s = %s" % (cname(d.name), self.init_value(d.initializer, t)))
                init = "%s %s" % (cpp_type(t0), ", ".join(parts))
            else:
                inits = c.init if isinstance(c.init, list) else [c.init]
                init = ", ".join(self.expr(e)[0] for e in inits)
        cond = self.expr(c.condition)[0] if c.condition is not None else ""
        upd = ", ".join(self.expr(e)[0] for e in (c.update or []))
        r = "%sfor (%s; %s; %s) {\n%s%s}\n" % (I, init, cond, upd, self.body(s.body, I + "  "), I)
        self.ctx.pop()
        return r

    def switch(self, s, I):
        sc, st = self.expr(s.expression)
        saved_in, saved_h = self.in_switch, self.ctx.hoist
        self.in_switch, self.ctx.hoist = True, []
        self.ctx.push()
        body = ""
        for case in s.cases:
            labels = ""
            for lab in case.case:
                labels += "%scase %s:\n" % (I + "  ", self.expr(lab)[0]) if lab is not None else ""
            if not case.case:
                labels = "%sdefault:\n" % (I + "  ")
            inner = "".join(self.stmt(x, I + "    ") for x in case.statements)
            if not inner:
                inner = "%s;\n" % (I + "    ")
            body += labels + inner
        self.ctx.pop()
        hoisted = "".join("%s  %s\n" % (I, h) for h in self.ctx.hoist)
        self.in_switch, self.ctx.hoist = saved_in, saved_h
        return "%s{\n%s%s  switch (%s) {\n%s%s  }\n%s}\n" % (I, hoisted, I, sc, body, I, I)


# ------------------------------------------------------------------ classes
def emit_class(name):
    tr = SYMS[name + ".tree"]
    cls = tr.types[0]
    E = Emit()
    hdr, src = [], []
    sup = cls.extends.name if cls.extends else None
    base = " : public %s" % sup if sup else ""
    hdr.append("class %s%s {\n public:\n" % (name, base))
    ctors = [m for m in cls.body if isinstance(m, T.ConstructorDeclaration)]
    statics = [m for m in cls.body if isinstance(m, list)]

    for m in cls.body:
        if isinstance(m, T.FieldDeclaration):
            is_static = "static" in m.modifiers
            for d in m.declarators:
                t0 = jtype(m.type)
                t = (t0[0], t0[1] + len(d.dimensions or []))
                E.ctx = Ctx(name, True, ("void", 0))
                E.in_switch = False
                init = ""
                if d.initializer is not None:
                    init = " = " + E.init_value(d.initializer, t)
                else:
                    di = default_init(t)
                    init = " = " + di if di else ""
                hdr.append("  %s%s %s%s;\n" % ("inline static " if is_static else "", cpp_type(t), cname(d.name), init))

    for m in ctors:
        ps = ", ".join("%s %s" % (cpp_type(jtype(p.type)), cname(p.name)) for p in m.parameters)
        hdr.append("  %s(%s);\n" % (name, ps))
        E.ctx = Ctx(name, False, ("void", 0))
        E.in_switch = False
        for p in m.parameters:
            E.ctx.declare(p.name, jtype(p.type))
        body = m.body
        initlist = ""
        first = body[0].expression if body and isinstance(body[0], T.StatementExpression) else (body[0] if body else None)
        if isinstance(first, T.SuperConstructorInvocation):
            cs, _ = E.args(first.arguments)
            initlist = " : %s(%s)" % (sup, ", ".join(cs))
            body = body[1:]
        src.append("%s::%s(%s)%s {\n%s}\n\n" % (name, name, ps, initlist, E.body_list(body, "  ")))

    for m in cls.body:
        if isinstance(m, T.MethodDeclaration):
            rt = jtype(m.return_type)
            is_static = "static" in m.modifiers
            ps = ", ".join("%s %s" % (cpp_type(jtype(p.type)), cname(p.name)) for p in m.parameters)
            virt = m.name in VIRTUALS and not is_static and sup == "Engine"
            hdr.append("  %s%s %s(%s)%s;\n" % ("static " if is_static else "", cpp_type(rt), cname(m.name), ps,
                                              " override" if virt else ""))
            E.ctx = Ctx(name, is_static, rt)
            E.in_switch = False
            for p in m.parameters:
                E.ctx.declare(p.name, jtype(p.type))
            src.append("%s %s::%s(%s) {\n%s}\n\n" % (cpp_type(rt), name, cname(m.name), ps,
                                                   E.body_list(m.body or [], "  ")))

    if statics:
        hdr.append("  static void staticInit();\n")
        E.ctx = Ctx(name, True, ("void", 0))
        E.in_switch = False
        body = "".join(E.body_list(b, "  ") for b in statics)
        src.append("void %s::staticInit() {\n%s}\n\n" % (name, body))
    hdr.append("};\n\n")
    return "".join(hdr), "".join(src)


def main():
    load_symbols()
    OUT.mkdir(parents=True, exist_ok=True)
    fwd = "".join("class %s;\n" % c for c in ALL_CLASSES if c not in ("Engine", "Sprite"))
    hdr_all = ['// Generated by tools/java2cpp.py from clonehome/src -- do not edit.\n#pragma once\n'
               '#include "../engine.h"\n#include "../platform.h"\n\nnamespace ch {\n\n', fwd, "\n"]
    # base classes first: Game derives from Engine (hand-written); others are plain
    for name in TRANSLATE:
        h, s = emit_class(name)
        hdr_all.append(h)
        (OUT / (name + ".cpp")).write_text(
            '// Generated by tools/java2cpp.py from clonehome/src -- do not edit.\n#include "classes.h"\n\n'
            "namespace ch {\n\n" + s + "}  // namespace ch\n", encoding="utf-8")
    hdr_all.append("}  // namespace ch\n")
    (OUT / "classes.h").write_text("".join(hdr_all), encoding="utf-8")
    print("wrote", OUT)
    if "--libcalls" in sys.argv:
        for c in sorted(LIBCALLS):
            print("  lib", c)
    print("%d statements flagged for evaluation-order review" % len(WARN))
    for k, s in WARN:
        print("  ", k, "|", s[:110])


if __name__ == "__main__":
    main()
