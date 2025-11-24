#pragma once

class ValuedTable {
private:
	int n;
	float* values;
	float lowerBoundary, upperBoundary, intervalLength;
	bool isCyclical;
	float (*function)(float a);

	float* generateTable();
public:
	ValuedTable(int n, float lowerBoundary, float upperBoundary, float (*f)(float), bool isCyclical);
	ValuedTable(int n, float lowerBoundary, float upperBoundary, float (*f)(float));
	int getLength();
	float getValue(int index);
	float getValue(float domainValue);
};