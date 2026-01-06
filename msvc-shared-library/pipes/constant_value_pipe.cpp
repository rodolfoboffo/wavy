#include "./constant_value_pipe.h"
#include "./port.h"

ConstantValuePipe::ConstantValuePipe() {
	Port outputPort = Port();
	this->ports[INPUT_PORTS_INDEX].push_back(outputPort);
}