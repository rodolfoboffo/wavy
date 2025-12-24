#include "sine.h"
#include "constants.h"
#include <cmath>

SineTable::SineTable(int n) : ValuedTable(n, 0, PI2, sin) {};

SineTable* SineTable_new(int n) {
	SineTable* s = new SineTable{ n };
	return s;
}

float SineTable_getValue(SineTable* s, int index) {
	float value = (*s).getValue(index);
	return value;
}

int SineTable_getLength(SineTable* s) {
	return s->getLength();
}
