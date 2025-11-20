#include <iostream>
#include <thread>
#include <tchar.h>
#include <windows.h>

typedef int (*pGetStatus)();
typedef int (*pGetCounter)();
typedef void (*pStartThread)();
typedef void (*pStopThread)();

int main() {
    HMODULE DLL = LoadLibrary(_T("wavy.dll"));
    std::cout << "Hello, World!" << std::endl;
    if (DLL) {
        std::cout<< "DLL loaded!" << std::endl;
        pGetStatus _getStatus = (pGetStatus)GetProcAddress(DLL,"wavy_getStatus");
        pGetCounter _getCounter = (pGetCounter)GetProcAddress(DLL,"wavy_getCounter");
        pStartThread _startThread = (pStartThread)GetProcAddress(DLL,"wavy_startThread");
        pStopThread _stopThread = (pStopThread)GetProcAddress(DLL,"wavy_stopThread");
        _startThread();
        std::this_thread::sleep_for(std::chrono::seconds(3));
        std::cout << "Counter:" << _getCounter() << std::endl;
        _stopThread();
        FreeLibrary(DLL);
    }
    std::cout << "Bye, World!" << std::endl;
    return 0;
}