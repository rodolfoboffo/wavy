#ifndef WAVY_SHARED_LIBRARY_LIBRARY_H
#define WAVY_SHARED_LIBRARY_LIBRARY_H

#include <windows.h>

#define DLLIMPORT_EXPORT __declspec(dllexport)

extern "C" {

    __declspec(dllexport) int wavy_getStatus();
    __declspec(dllexport) int wavy_getCounter();
    __declspec(dllexport) void wavy_startThread();
    __declspec(dllexport) void wavy_stopThread();

}

#endif // WAVY_SHARED_LIBRARY_LIBRARY_H