#pragma once
#include "../wavy.h"
#include "./buffer.h"
#include <mutex>

#define INPUT_PORTS_INDEX 0U
#define OUTPUT_PORTS_INDEX 1U

class Pipe;

class Port {
private:
	std::mutex* mtx;
	Pipe* pipe;
	Port* linkedPort;
	Buffer* buffer;
protected:
	const char* name;
public:
	Port(Pipe* p, const char* name);
	const char* getName();
	void setLinkedPort(Port* p);
	Port* getLinkedPort();
	bool putValue(float v);
};

class InputPort : public Port {
public:
	InputPort(Pipe* p, const char* name) : Port(p, name) {};
};

class OutputPort : public Port {
public:
	OutputPort(Pipe* p, const char* name) : Port(p, name) {};
};

extern "C" {
	WAVYLIBRARY_API const char* Port_getName(Port* p);
	WAVYLIBRARY_API void Port_setLinkedPort(Port* p1, Port* p2);
	WAVYLIBRARY_API Port* Port_getLinkedPort(Port* p);
}