#include "./buffer.h"

Buffer::Buffer(unsigned int capacity, bool endless) {
	this->mtx = new std::mutex();
	this->capacity = capacity;
	this->endless = endless;
	this->array = (float*)malloc((this->capacity+1) * sizeof(float));
	this->startIndex = this->finishIndex = 0;
}

Buffer::Buffer(bool endless) : Buffer::Buffer(DEFAULT_BUFFER_SIZE, endless) {}

Buffer::Buffer() : Buffer::Buffer(DEFAULT_BUFFER_SIZE, false) {}

bool Buffer::isFull()
{
	return this->finishIndex == (this->startIndex - 1 + (this->capacity+1)) % (this->capacity + 1);
}

void Buffer::put(float value)
{
    std::lock_guard<std::mutex> lock(*(this->mtx));
    boolean _isFull = this->isFull();
    if (!this->endless) {
        if (_isFull)
            throw std::exception("Buffer is full.");
        this->array[this->finishIndex] = value;
        this->finishIndex = (this->finishIndex + 1) % (this->capacity + 1);
    }
    else {
        this->array[this->finishIndex] = value;
        this->finishIndex = (this->finishIndex + 1) % (this->capacity + 1);
        if (_isFull) {
            this->startIndex = (this->startIndex + 1) % (this->capacity + 1);
        }
    }
}
