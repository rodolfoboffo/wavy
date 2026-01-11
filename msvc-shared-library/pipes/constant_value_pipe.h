#pragma once

#include "./pipe.h"

class ConstantValuePipe : public Pipe {
private:
	float value;
protected:
	void createPorts() override;
	void process() override;
public:
	ConstantValuePipe();
	ConstantValuePipe(float v);
	void setValue(float v);
};

extern "C" {
	WAVYLIBRARY_API ConstantValuePipe* ConstantValuePipe_new();
	WAVYLIBRARY_API ConstantValuePipe* ConstantValuePipe_setValue(float v);
}