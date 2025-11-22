#pragma once

#define WAVYLIBRARY_API __declspec(dllexport)

extern "C" {
    WAVYLIBRARY_API int wavy_getStatus();
    WAVYLIBRARY_API int wavy_getCounter();
    WAVYLIBRARY_API void wavy_startThread();
    WAVYLIBRARY_API void wavy_stopThread();
}