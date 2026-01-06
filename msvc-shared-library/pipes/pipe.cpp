#include "./pipe.h"
#include "./port.h"

unsigned int Pipe::getPortsCount(int index)
{
	return (unsigned int)this->ports[index].size();
}

Pipe::Pipe() {
}

unsigned int Pipe::getInputPortsCount()
{
	return this->getPortsCount(INPUT_PORTS_INDEX);
}

unsigned int Pipe::getOutputPortsCount()
{
	return this->getPortsCount(OUTPUT_PORTS_INDEX);
}

Pipe* Pipe_new() {
	Pipe* p = new Pipe();
	return p;
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
