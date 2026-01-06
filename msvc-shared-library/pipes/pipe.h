#pragma once

#include "../wavy.h"
#include <list>

class Port;

class Pipe {
protected:
	std::list<Port> ports[2];
	unsigned int getPortsCount(int index);
public:
	Pipe();
	unsigned int getInputPortsCount();
	unsigned int getOutputPortsCount();
};

extern "C" {
	WAVYLIBRARY_API Pipe* Pipe_new();
	WAVYLIBRARY_API void Pipe_free(Pipe* p);
	WAVYLIBRARY_API unsigned int Pipe_getInputPortsCount(Pipe* p);
	WAVYLIBRARY_API unsigned int Pipe_getOutputPortsCount(Pipe* p);
}