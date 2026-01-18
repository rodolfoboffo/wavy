#pragma once

#include <windows.h>
#define WAVYLIBRARY_API __declspec(dllexport)

#define _LOGGING_LEVEL_DEBUG 0 
#define _LOGGING_LEVEL_WARN 1
#define _LOGGING_LEVEL_INFO 2
#define _LOGGING_LEVEL_NOTHING 99

#ifndef _DEBUG
#define _LOGGING_LEVEL _LOGGING_LEVEL_NOTHING
#else
#define _LOGGING_LEVEL _LOGGING_LEVEL_INFO
#endif