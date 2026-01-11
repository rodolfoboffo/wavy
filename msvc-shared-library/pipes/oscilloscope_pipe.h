#pragma once

#include "./pipe.h"

class OscilloscopePipe : public Pipe {
protected:
	void createPorts() override;
public:
	OscilloscopePipe();
};

extern "C" {
	WAVYLIBRARY_API OscilloscopePipe* OscilloscopePipe_new();
}