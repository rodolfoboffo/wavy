#include "./constant_value_pipe.h"
#include "./port.h"

#ifdef _DEBUG
#include <iostream>
#endif

ConstantValuePipe::ConstantValuePipe() : Pipe() {
	this->createPorts();
};

void ConstantValuePipe::createPorts() {
#ifdef _DEBUG
	std::cout << "Creating ports for ConstantValuePipe." << std::endl;
#endif
	Port* outputPort = new Port();
	this->ports[OUTPUT_PORTS_INDEX].push_back(outputPort);
}

ConstantValuePipe* ConstantValuePipe_new()
{
	#ifdef _DEBUG
	std::cout << "ConstantValuePipe_new called." << std::endl;
	#endif
	return new ConstantValuePipe();
}
