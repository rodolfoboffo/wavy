#include "pch.h"
#include "wavy.h"
#include <thread>

std::thread t;
int quit = 0;
int status = 0;
int counter = 0;

int wavy_getStatus() {
    return status;
}

int wavy_getCounter() {
    return counter--;
}

void counter_callable() {
    while (quit == 0) {
        status = 4;
        counter++;
        std::this_thread::sleep_for(std::chrono::milliseconds(100));
    }
}

void wavy_stopThread() {
    quit = 1;
    t.join();
}

void wavy_startThread() {
    status = 2;
    t = std::thread{ counter_callable };
    status = 3;
}
