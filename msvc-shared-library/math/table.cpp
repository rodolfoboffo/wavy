#include "table.h"
#include <cmath>

ValuedTable::ValuedTable(int n, float lowerBoundary, float upperBoundary, float (*f)(float), bool isCyclical) : n(n), lowerBoundary(lowerBoundary), upperBoundary(upperBoundary), function(f), isCyclical(isCyclical) {
	this->upperBoundary = fmax(upperBoundary, lowerBoundary);
	this->lowerBoundary = fmin(upperBoundary, lowerBoundary);
	this->intervalLength = this->upperBoundary - this->lowerBoundary;
	this->values = generateTable();
}

ValuedTable::ValuedTable(int n, float lowerBoundary, float upperBoundary, float (*f)(float)) : ValuedTable(n, lowerBoundary, upperBoundary, f, true) {};

float* ValuedTable::generateTable() {
	float* values = new float[this->n];
	for (int i = 0; i < this->n; i++) {
		values[i] = this->function((this->intervalLength) / (float)this->n * i + this->lowerBoundary);
	}
	return values;
}

int ValuedTable::getLength() {
	return this->n;
}

float ValuedTable::getValue(int index) {
	if (!this->isCyclical && (index < 0 || index >= this->n))
		return -1.0f;
	int i = index % this->n;
	return this->values[i];
}

float ValuedTable::getValue(float domainValue) {
	if (!this->isCyclical && (domainValue < this->lowerBoundary || domainValue > this->upperBoundary))
		throw "Valued Table out of boundaries.";
	float v = remainder(float(domainValue - this->lowerBoundary), (this->intervalLength) / (this->intervalLength));
	v = v >= 0 ? v : 1 + v;
	int i = (int)(v * this->n);
	return this->values[i];
}
