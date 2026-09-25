#include "jrt.h"

namespace ch {

String String::trim() const {
  const std::u16string& s = str();
  size_t a = 0, b = s.size();
  while (a < b && s[a] <= u' ') a++;
  while (b > a && s[b - 1] <= u' ') b--;
  return String(s.substr(a, b - a));
}

String String::fromUtf8(const std::string& s) {
  std::u16string o;
  for (size_t i = 0; i < s.size();) {
    unsigned char c = (unsigned char)s[i];
    if (c < 0x80) { o += (char16_t)c; i++; }
    else if (c < 0xE0 && i + 1 < s.size()) { o += (char16_t)((c & 31) << 6 | (s[i + 1] & 63)); i += 2; }
    else if (c < 0xF0 && i + 2 < s.size()) { o += (char16_t)((c & 15) << 12 | (s[i + 1] & 63) << 6 | (s[i + 2] & 63)); i += 3; }
    else { i++; }
  }
  return String(o);
}

std::string String::toUtf8() const {
  std::string o;
  for (char16_t c : str()) {
    if (c < 0x80) o += (char)c;
    else if (c < 0x800) { o += (char)(0xC0 | c >> 6); o += (char)(0x80 | (c & 63)); }
    else { o += (char)(0xE0 | c >> 12); o += (char)(0x80 | (c >> 6 & 63)); o += (char)(0x80 | (c & 63)); }
  }
  return o;
}

}  // namespace ch
