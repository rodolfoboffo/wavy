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
public:
	Port(Pipe* p);
	void setLinkedPort(Port* p);
	Port* getLinkedPort();
	bool putValue(float v);
};