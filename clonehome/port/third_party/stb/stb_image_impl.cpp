// Compiles stb_image's implementation into one translation unit -- see
// PROVENANCE.md. Clone Home's images are PNG, except the splash screen
// (resource 1067), which is a JPEG, so only those two decoders are kept.
// STBI_NO_STDIO: images are always decoded from an in-memory resource.
#define STB_IMAGE_IMPLEMENTATION
#define STBI_NO_STDIO
#define STBI_NO_BMP
#define STBI_NO_PSD
#define STBI_NO_TGA
#define STBI_NO_GIF
#define STBI_NO_HDR
#define STBI_NO_PIC
#define STBI_NO_PNM
#include "stb_image.h"
