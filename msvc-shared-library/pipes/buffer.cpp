#include "./buffer.h"

template <typename T>
Buffer<T>::Buffer(unsigned int capacity, bool endless) {
	this->capacity = capacity;
	this->endless = endless;
	this->buffer = malloc((this->capacity+1) * sizeof(T));
	this->startIndex = this->finishIndex = 0;
}

template <typename T>
Buffer<T>::Buffer(bool endless) : Buffer<T>::Buffer(DEFAULT_BUFFER_SIZE, endless) {}

template <typename T>
void Buffer<T>::clear() {
	this->startIndex = this->finishIndex = 0;
}