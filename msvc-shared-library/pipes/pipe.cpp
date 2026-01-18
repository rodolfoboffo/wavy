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
	this->mtx = new std::mutex();
	this->running = true;
	this->isShuttingDown = false;
	this->threadedWorker = true;
}

void Pipe::createAndStartWorker() {
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_INFO
	std::cout << "Creating worker thread for Pipe." << std::endl;
#endif
	this->workerThread = new std::thread(&Pipe::workerTask, this);
}

void Pipe::workerTask() {
	bool _keepRunning = true;
	while (_keepRunning) {
		if (!this->isShuttingDown) {
			if (this->running) {
				this->process();
			}
			else {
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_DEBUG
				std::cout << "Pipe paused. Worker task doing nothing." << std::endl;
#endif
			}
			std::this_thread::yield();
		}
		else {
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_INFO
			std::cout << "Worker thread finishing." << std::endl;
#endif
			_keepRunning = false;
		}
	}
}

void Pipe::process() {
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_DEBUG
	std::cout << "Worker task doing job." << std::endl;
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
	std::lock_guard<std::mutex> lock(*(this->mtx));
	this->createPorts();
	if (this->threadedWorker)
		this->createAndStartWorker();
}

void Pipe::setRunning(bool r)
{
	std::lock_guard<std::mutex> lock(*(this->mtx));
	this->running = r;
}

void Pipe::shutdown()
{
	this->mtx->lock();
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_INFO
	std::cout << "Pipe shutting down." << std::endl;
#endif
	this->running = false;
	this->isShuttingDown = true;
	this->mtx->unlock();
	if (this->threadedWorker && this->workerThread != NULL) {
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_INFO
		std::cout << "Waiting worker thread to join." << std::endl;
#endif
		this->workerThread->join();
#if _LOGGING_LEVEL <= _LOGGING_LEVEL_INFO
		std::cout << "Worker thread joined." << std::endl;
#endif
	}
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

void Pipe_setRunning(Pipe* p, bool r)
{
	p->setRunning(r);
}

unsigned short Pipe_getInputPortsCount(Pipe* p)
{
	return p->getInputPortsCount();
}

unsigned short Pipe_getOutputPortsCount(Pipe* p)
{
	return p->getOutputPortsCount();
}

Port* Pipe_getInputPort(Pipe* p, unsigned short portIndex)
{
	return p->getInputPort(portIndex);
}

Port* Pipe_getOutputPort(Pipe* p, unsigned short portIndex)
{
	return p->getOutputPort(portIndex);
}
