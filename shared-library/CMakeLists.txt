cmake_minimum_required(VERSION 4.0)
project(shared_library)

set(CMAKE_CXX_STANDARD 20)

add_library(shared_library SHARED wavy_library.cpp)
