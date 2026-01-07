#pragma once

#include "./pipe.h"

class ConstantValuePipe : public Pipe {
protected:
	void createPorts() override;
public:
	ConstantValuePipe();
};

extern "C" {
	WAVYLIBRARY_API ConstantValuePipe* ConstantValuePipe_new();
}