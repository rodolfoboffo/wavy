#pragma once

#define INPUT_PORTS_INDEX 0
#define OUTPUT_PORTS_INDEX 1

class Pipe;

class Port {
private:
	Pipe* pipe;
	Port* linkedPort;
};