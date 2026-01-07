#include "./pipe.h"
#include "./port.h"

#ifdef _DEBUG
#include <iostream>
#endif

std::vector<Port*> Pipe::getPorts(unsigned short inputOutput)
{
	return this->ports[inputOutput];
}

Port* Pipe::getPort(unsigned short inputOutput, unsigned short portIndex)
{
	size_t count = this->getPortsCount(inputOutput);
	if (count > 0 && count > portIndex)
		return this->ports[inputOutput][portIndex];
	return nullptr;
}

unsigned int Pipe::getPortsCount(unsigned short inputOutput)
{
	return (unsigned int)this->ports[inputOutput].size();
}

Pipe::Pipe() {
	this->threadedWorker = true;
}

void Pipe::createAndStartWorker() {

}

unsigned int Pipe::getInputPortsCount()
{
	return this->getPortsCount(INPUT_PORTS_INDEX);
}

unsigned int Pipe::getOutputPortsCount()
{
	return this->getPortsCount(OUTPUT_PORTS_INDEX);
}

std::vector<Port*> Pipe::getInputPorts()
{
	return this->getPorts(INPUT_PORTS_INDEX);
}

std::vector<Port*> Pipe::getOutputPorts()
{
	return this->getPorts(OUTPUT_PORTS_INDEX);
}

Port* Pipe::getInputPort()
{
	return this->getInputPort(0);
}

Port* Pipe::getOutputPort()
{
	return this->getOutputPort(0);
}

Port* Pipe::getInputPort(unsigned short portIndex)
{
	return this->getPort(INPUT_PORTS_INDEX, portIndex);
}

Port* Pipe::getOutputPort(unsigned short portIndex)
{
	return this->getPort(OUTPUT_PORTS_INDEX, portIndex);
}

void Pipe::init()
{
	this->createPorts();
	if (this->threadedWorker)
		this->createAndStartWorker();
}

void Pipe_free(Pipe* p) {
	delete p;
}

unsigned int Pipe_getInputPortsCount(Pipe* p)
{
	return p->getInputPortsCount();
}

unsigned int Pipe_getOutputPortsCount(Pipe* p)
{
	return p->getOutputPortsCount();
}
