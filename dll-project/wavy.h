#pragma once
#include <windows.h>

#ifdef CPPDLL_EXPORTS
#define DLLIMPORT_EXPORT __declspec(dllexport)
#else
#define DLLIMPORT_EXPORT __declspec(dllimport)
#endif

extern "C" {
	DLLIMPORT_EXPORT int wavy_numberTwo();
}