#pragma once

#define INPUT_PORTS_INDEX 0U
#define OUTPUT_PORTS_INDEX 1U

class Pipe;

class Port {
private:
	Pipe* pipe;
	Port* linkedPort;
};