#include "./port.h"

#ifdef _DEBUG
#include <iostream>
#endif

Port::Port(Pipe* p, const char* name) {
	this->mtx = new std::mutex();
	this->name = name;
	this->pipe = p;
	this->buffer = new Buffer();
	this->linkedPort = nullptr;
}

const char* Port::getName()
{
	return this->name;
}

void Port::setLinkedPort(Port* p)
{
	std::lock_guard<std::mutex> lock(*(this->mtx));
	Port* previousLinkedPort = this->linkedPort;
	this->linkedPort = p;
	if (previousLinkedPort != nullptr && p != previousLinkedPort) {
		previousLinkedPort->setLinkedPort(nullptr);
	}
	if (p != nullptr && p->getLinkedPort() != this) {
		p->setLinkedPort(this);
	}
}

Port* Port::getLinkedPort()
{
	return this->linkedPort;
}

bool Port::putValue(float v)
{
	std::lock_guard<std::mutex> lock(*(this->mtx));
	if (this->linkedPort != nullptr) {
		if (!this->linkedPort->buffer->isFull()) {
			this->linkedPort->buffer->put(v);
			return true;
		}
		else {
#ifdef _DEBUG
			std::cout << "Buffer is full." << std::endl;
#endif
		}
	}
	return false;
}

const char* Port_getName(Port* p)
{
	return p->getName();
}
