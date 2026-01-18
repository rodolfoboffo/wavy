#include "./oscilloscope_pipe.h"
#include "./port.h"

#ifdef _DEBUG
#include <iostream>
#endif

void OscilloscopePipe::createPorts()
{
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_INFO
	std::cout << "Creating ports for OscilloscopePipe." << std::endl;
#endif
	Port* inputPort = new InputPort(this, "Input");
	this->ports[INPUT_PORTS_INDEX].push_back(inputPort);
}

OscilloscopePipe::OscilloscopePipe() {}

OscilloscopePipe* OscilloscopePipe_new()
{
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_INFO
	std::cout << "OscilloscopePipe_new called." << std::endl;
#endif
	return new OscilloscopePipe();
}
