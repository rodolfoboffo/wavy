#pragma once

#include "../wavy.h"
#include <vector>
#include <thread>

class Port;

class Pipe {
private:
	bool threadedWorker;
	std::thread* workerThread;
protected:
	std::vector<Port*> ports[2];
	std::vector<Port*> getPorts(unsigned short inputOutput);
	Port* getPort(unsigned short inputOutput, unsigned short portIndex);
	unsigned int getPortsCount(unsigned short inputOutput);
	virtual void createPorts() = 0;
	void createAndStartWorker();
	virtual void workerTask() = 0;
public:
	Pipe();
	unsigned int getInputPortsCount();
	unsigned int getOutputPortsCount();
	std::vector<Port*> getInputPorts();
	std::vector<Port*> getOutputPorts();
	Port* getInputPort();
	Port* getOutputPort();
	Port* getInputPort(unsigned short portIndex);
	Port* getOutputPort(unsigned short portIndex);
	void init();
};

extern "C" {
	WAVYLIBRARY_API void Pipe_free(Pipe* p);
	WAVYLIBRARY_API unsigned int Pipe_getInputPortsCount(Pipe* p);
	WAVYLIBRARY_API unsigned int Pipe_getOutputPortsCount(Pipe* p);
}