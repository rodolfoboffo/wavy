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
	this->workerThread = NULL;
	this->mtx = new std::recursive_mutex();
	this->running = true;
	this->threadedWorker = true;
}

void Pipe::createAndStartWorker() {
#ifdef _DEBUG
	std::cout << "Creating worker thread for Pipe." << std::endl;
#endif
	this->workerThread = new std::thread(&Pipe::workerTask, this);
}

void Pipe::workerTask() {
	while (this->running) {
#ifdef _DEBUG
		std::cout << "Worker task doing nothing." << std::endl;
#endif
		std::this_thread::sleep_for(std::chrono::seconds(5));
	}
#ifdef _DEBUG
	std::cout << "Worker task finishing." << std::endl;
#endif
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
	this->mtx->lock();
	if (this->threadedWorker)
		this->createAndStartWorker();
	this->mtx->unlock();
}

void Pipe::shutdown()
{
	this->mtx->lock();
#ifdef _DEBUG
	std::cout << "Pipe shutting down." << std::endl;
#endif
	this->running = false;
	if (this->threadedWorker && this->workerThread != NULL) {
#ifdef _DEBUG
		std::cout << "Waiting worker thread to join." << std::endl;
#endif
		this->workerThread->join();
#ifdef _DEBUG
		std::cout << "Worker thread joined." << std::endl;
#endif
	}
	this->mtx->unlock();
}

void Pipe_free(Pipe* p) {
	delete p;
}

void Pipe_shutdown(Pipe* p) {
	p->shutdown();
}

void Pipe_init(Pipe* p) {
	p->init();
}

unsigned int Pipe_getInputPortsCount(Pipe* p)
{
	return p->getInputPortsCount();
}

unsigned int Pipe_getOutputPortsCount(Pipe* p)
{
	return p->getOutputPortsCount();
}
