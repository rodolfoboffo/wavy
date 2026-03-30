#include "./port.h"
#include <iostream>

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
	if (p == this) {
		return;
	}

	// Break the old bidirectional link
	if (this->linkedPort != nullptr) {
		this->linkedPort->linkedPort = nullptr;
	}

	this->linkedPort = p;
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_INFO
	if (p != nullptr)
		std::cout << "Port " << this->name << " is now linked to port " << p->name << "." << std::endl;
	else
		std::cout << "Port " << this->name << " is now unlinked." << std::endl;
#endif

	// Establish new bidirectional link
	if (p != nullptr) {
		if (p->linkedPort != nullptr) {
			p->linkedPort->linkedPort = nullptr;
		}
		p->linkedPort = this;
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_INFO
		std::cout << "Port " << p->name << " is now linked to port " << this->name << "." << std::endl;
#endif
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
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_DEBUG
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

void Port_setLinkedPort(Port* p1, Port* p2)
{
	return p1->setLinkedPort(p2);
}

Port* Port_getLinkedPort(Port* p)
{
	return p->getLinkedPort();
}

