#pragma once
#include <windows.h>
#include "math/sine.h"

#define WAVYLIBRARY_API __declspec(dllexport)

extern "C" {
    WAVYLIBRARY_API SineTable* SineTable_new(int n);
    WAVYLIBRARY_API float SineTable_getValue(SineTable *s, int index);
    WAVYLIBRARY_API int SineTable_getLength(SineTable* s);
    
}