#pragma once
#include "../wavy.h"
#include "table.h"

class SineTable : public ValuedTable {
public:
	SineTable(int n);
};

extern "C" {
    WAVYLIBRARY_API SineTable* SineTable_new(int n);
    WAVYLIBRARY_API float SineTable_getValue(SineTable* s, int index);
    WAVYLIBRARY_API int SineTable_getLength(SineTable* s);
}