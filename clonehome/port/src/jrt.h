// Minimal Java runtime for the code produced by tools/java2cpp.py:
// nullable reference arrays, immutable UTF-16 strings, DataInputStream,
// java.util.Random and a few java.lang helpers. Exceptions thrown here
// (array bounds, EOF) stand in for the Java ones the game code catches.
#pragma once
#include <chrono>
#include <cstdint>
#include <cstdlib>
#include <initializer_list>
#include <memory>
#include <string>
#include <vector>

namespace ch {

struct JavaException {
  const char* what;
};

// ---------------------------------------------------------------- arrays
template <class T>
class Arr {
 public:
  Arr() = default;
  Arr(std::nullptr_t) {}
  explicit Arr(int n) : n_(n), p_(new T[(size_t)n]()) {}
  template <class... A>
  static Arr make(A... v) {
    Arr a((int)sizeof...(A));
    int i = 0;
    ((a.p_.get()[i++] = static_cast<T>(v)), ...);
    return a;
  }
  T& operator[](int i) const {
    if (i < 0 || i >= n_) throw JavaException{"ArrayIndexOutOfBounds"};
    return p_.get()[i];
  }
  int length() const { return n_; }
  bool operator==(std::nullptr_t) const { return !p_; }
  bool operator!=(std::nullptr_t) const { return (bool)p_; }
  bool operator==(const Arr& o) const { return p_ == o.p_; }
  bool operator!=(const Arr& o) const { return p_ != o.p_; }
  T* data() const { return p_.get(); }

 private:
  int n_ = 0;
  std::shared_ptr<T[]> p_;
};

template <class T>
Arr<T> newArr(int n) { return Arr<T>(n); }
template <class T>
Arr<Arr<T>> newArr2(int n, int m) {
  Arr<Arr<T>> a(n);
  if (m >= 0) for (int i = 0; i < n; i++) a[i] = Arr<T>(m);
  return a;
}

template <class T>
void arraycopy(const Arr<T>& src, int sp, const Arr<T>& dst, int dp, int len) {
  if (sp < 0 || dp < 0 || len < 0 || sp + len > src.length() || dp + len > dst.length())
    throw JavaException{"ArrayIndexOutOfBounds"};
  if (src.data() == dst.data() && sp < dp) {
    for (int i = len - 1; i >= 0; i--) dst[dp + i] = src[sp + i];
  } else {
    for (int i = 0; i < len; i++) dst[dp + i] = src[sp + i];
  }
}

// --------------------------------------------------------------- strings
class String {
 public:
  String() = default;
  String(std::nullptr_t) {}
  String(const char16_t* s) : p_(std::make_shared<const std::u16string>(s)) {}
  String(std::u16string s) : p_(std::make_shared<const std::u16string>(std::move(s))) {}
  explicit String(const Arr<int8_t>& bytes) {  // new String(byte[]): Latin-1 stand-in for the default charset
    std::u16string u;
    for (int i = 0; i < bytes.length(); i++) u += (char16_t)(uint8_t)bytes[i];
    p_ = std::make_shared<const std::u16string>(std::move(u));
  }
  explicit String(const Arr<char16_t>& chars)
      : p_(std::make_shared<const std::u16string>(chars.data(), (size_t)chars.length())) {}
  void getChars(int from, int to, const Arr<char16_t>& dst, int at) const {
    for (int i = from; i < to; i++) dst[at + i - from] = charAt(i);
  }
  bool isNull() const { return !p_; }
  bool operator==(std::nullptr_t) const { return !p_; }
  bool operator!=(std::nullptr_t) const { return (bool)p_; }
  bool operator==(const String& o) const { return p_ == o.p_; }  // reference equality, as in Java
  bool operator!=(const String& o) const { return p_ != o.p_; }
  const std::u16string& str() const {
    static const std::u16string kNull = u"null";
    return p_ ? *p_ : kNull;
  }
  int length() const { return (int)str().size(); }
  char16_t charAt(int i) const {
    if (!p_ || i < 0 || i >= (int)p_->size()) throw JavaException{"StringIndexOutOfBounds"};
    return (*p_)[(size_t)i];
  }
  bool equals(const String& o) const { return p_ && o.p_ && *p_ == *o.p_; }
  String substring(int a) const { return substring(a, length()); }
  String substring(int a, int b) const {
    if (a < 0 || b > length() || a > b) throw JavaException{"StringIndexOutOfBounds"};
    return String(str().substr((size_t)a, (size_t)(b - a)));
  }
  int indexOf(int c) const { return indexOf(c, 0); }
  int indexOf(int c, int from) const {
    size_t r = str().find((char16_t)c, (size_t)(from < 0 ? 0 : from));
    return r == std::u16string::npos ? -1 : (int)r;
  }
  String trim() const;
  String toUpperCase() const {
    std::u16string o = str();
    for (auto& c : o) if (c >= u'a' && c <= u'z') c = (char16_t)(c - 32);
    return String(o);
  }
  String toLowerCase() const {
    std::u16string o = str();
    for (auto& c : o) if (c >= u'A' && c <= u'Z') c = (char16_t)(c + 32);
    return String(o);
  }
  int lastIndexOf(int c, int from) const {
    size_t r = str().rfind((char16_t)c, from < 0 ? std::u16string::npos : (size_t)from);
    return r == std::u16string::npos ? -1 : (int)r;
  }
  int lastIndexOf(int c) const {
    size_t r = str().rfind((char16_t)c);
    return r == std::u16string::npos ? -1 : (int)r;
  }
  Arr<char16_t> toCharArray() const {
    Arr<char16_t> a(length());
    for (int i = 0; i < length(); i++) a[i] = str()[(size_t)i];
    return a;
  }
  static String valueOf(int v) { return fromAscii(std::to_string(v)); }
  static String valueOf(int64_t v) { return fromAscii(std::to_string(v)); }
  static String valueOf(char16_t c) { return String(std::u16string(1, c)); }
  static String valueOf(const Arr<char16_t>& a) { return String(std::u16string(a.data(), (size_t)a.length())); }
  static String fromAscii(const std::string& s) { return String(std::u16string(s.begin(), s.end())); }
  static String fromUtf8(const std::string& s);
  std::string toUtf8() const;

 private:
  std::shared_ptr<const std::u16string> p_;
};

inline String jstr(const String& s) { return s; }
inline String jstr(int v) { return String::valueOf(v); }
inline String jstr(int64_t v) { return String::valueOf(v); }
inline String jstr(char16_t c) { return String::valueOf(c); }
inline String jstr(bool b) { return b ? String(u"true") : String(u"false"); }
inline String operator+(const String& a, const String& b) { return String(a.str() + b.str()); }

class StringBuffer {
 public:
  StringBuffer() = default;
  explicit StringBuffer(int) {}
  StringBuffer* append(int v) { s_ += String::valueOf(v).str(); return this; }
  StringBuffer* append(char16_t c) { s_ += c; return this; }
  StringBuffer* append(const String& v) { s_ += v.str(); return this; }
  int length() const { return (int)s_.size(); }
  String toString() const { return String(s_); }

 private:
  std::u16string s_;
};

// ------------------------------------------------------------ java.lang
namespace jlang {
template <class T>
inline T abs(T v) { return v < 0 ? (T)-v : v; }
template <class T>
inline T max(T a, T b) { return a > b ? a : b; }
template <class T>
inline T min(T a, T b) { return a < b ? a : b; }
inline int64_t currentTimeMillis() {
  using namespace std::chrono;
  return duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();
}
inline bool isDigit(char16_t c) { return c >= u'0' && c <= u'9'; }
inline int digit(char16_t c, int radix) {
  int v = (c >= u'0' && c <= u'9') ? c - u'0' : (c >= u'a' && c <= u'z') ? c - u'a' + 10 : (c >= u'A' && c <= u'Z') ? c - u'A' + 10 : -1;
  return v < radix ? v : -1;
}
inline void gc() {}
}  // namespace jlang

// java.util.Random (same LCG as the JDK).
class Random {
 public:
  Random() : Random(jlang::currentTimeMillis()) {}
  explicit Random(int64_t seed) : seed_((seed ^ 0x5DEECE66DLL) & ((1LL << 48) - 1)) {}
  int next(int bits) {
    seed_ = (seed_ * 0x5DEECE66DLL + 0xBLL) & ((1LL << 48) - 1);
    return (int)(seed_ >> (48 - bits));
  }
  int nextInt() { return next(32); }
  int nextInt(int bound) {
    if (bound <= 0) throw JavaException{"IllegalArgument"};
    if ((bound & -bound) == bound) return (int)(((int64_t)bound * (int64_t)next(31)) >> 31);
    int bits, val;
    do {
      bits = next(31);
      val = bits % bound;
    } while (bits - val + (bound - 1) < 0);
    return val;
  }
  bool nextBoolean() { return next(1) != 0; }

 private:
  int64_t seed_;
};

// java.io.DataInputStream over an in-memory range.
class DataInputStream {
 public:
  DataInputStream(const uint8_t* p, int n) : p_(p), n_(n) {}
  int read() { return pos_ < n_ ? p_[pos_++] : -1; }
  int8_t readByte() { return (int8_t)need(1)[0]; }
  int readUnsignedByte() { return need(1)[0]; }
  int16_t readShort() { const uint8_t* b = need(2); return (int16_t)(b[0] << 8 | b[1]); }
  char16_t readChar() { const uint8_t* b = need(2); return (char16_t)(b[0] << 8 | b[1]); }
  int readInt() { const uint8_t* b = need(4); return (int)((uint32_t)b[0] << 24 | b[1] << 16 | b[2] << 8 | b[3]); }
  bool readBoolean() { return readByte() != 0; }
  void readFully(const Arr<int8_t>& a) { readFully(a, 0, a.length()); }
  void readFully(const Arr<int8_t>& a, int off, int len) {
    const uint8_t* b = need(len);
    for (int i = 0; i < len; i++) a[off + i] = (int8_t)b[i];
  }
  int64_t skip(int64_t n) {
    int64_t k = n < 0 ? 0 : (n > n_ - pos_ ? n_ - pos_ : n);
    pos_ += (int)k;
    return k;
  }
  int available() const { return n_ - pos_; }
  void close() {}

 private:
  const uint8_t* need(int k) {
    if (pos_ + k > n_) throw JavaException{"EOF"};
    const uint8_t* r = p_ + pos_;
    pos_ += k;
    return r;
  }
  const uint8_t* p_;
  int n_, pos_ = 0;
};

}  // namespace ch
