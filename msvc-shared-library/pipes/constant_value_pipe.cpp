#include "./constant_value_pipe.h"
#include "./port.h"

#ifdef _DEBUG
#include <iostream>
#endif

ConstantValuePipe::ConstantValuePipe() : ConstantValuePipe::ConstantValuePipe(0.0f) {}
ConstantValuePipe::ConstantValuePipe(float v) : Pipe() {
	this->value = v;
}
void ConstantValuePipe::setValue(float v)
{
	std::lock_guard<std::mutex> lock(*(this->mtx));
	this->value = v;
}
;

void ConstantValuePipe::createPorts() {
#ifdef _DEBUG
	std::cout << "Creating ports for ConstantValuePipe." << std::endl;
#endif
	Port* outputPort = new Port(this);
	this->ports[OUTPUT_PORTS_INDEX].push_back(outputPort);
}

void ConstantValuePipe::process() {
#ifdef _DEBUG
	std::cout << "ConstantValuePipe Worker task doing job." << std::endl;
#endif
	std::lock_guard<std::mutex> lock(*(this->mtx));
	if (this->getOutputPort() != nullptr)
		this->getOutputPort()->putValue(this->value);
}

ConstantValuePipe* ConstantValuePipe_new()
{
	#ifdef _DEBUG
	std::cout << "ConstantValuePipe_new called." << std::endl;
	#endif
	return new ConstantValuePipe();
}
