#include "./oscilloscope_pipe.h"
#include "./port.h"

#ifdef _DEBUG
#include <iostream>
#endif

void OscilloscopePipe::createPorts()
{
#ifdef _DEBUG
	std::cout << "Creating ports for OscilloscopePipe." << std::endl;
#endif
	Port* inputPort = new Port(this);
	this->ports[INPUT_PORTS_INDEX].push_back(inputPort);
}

OscilloscopePipe::OscilloscopePipe() {}

OscilloscopePipe* OscilloscopePipe_new()
{
#ifdef _DEBUG
	std::cout << "OscilloscopePipe_new called." << std::endl;
#endif
	return new OscilloscopePipe();
}
