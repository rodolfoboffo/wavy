#pragma once

#include "../wavy.h"
#include <mutex>

#define DEFAULT_BUFFER_SIZE 102400U

template <typename T>
class Buffer {
protected:
	std::recursive_mutex* mutex;
	T* buffer;
	bool endless;
	unsigned int capacity, startIndex, finishIndex;
public:
	Buffer(unsigned int capacity, bool endless);
	Buffer(bool endless);
	Buffer();
	unsigned int getCapacity();
	unsigned int getRemainingCapacity();
	T pickOne();
	void getValues(unsigned int count, T* array);
	void getValue(unsigned int index);
	void fetch(unsigned int count);
	bool isFull();
	bool isEmpty();
	void put(T value);
	void putAll(Buffer<T> b);
	void clear();
};
