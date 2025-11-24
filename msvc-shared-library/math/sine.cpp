#include "sine.h"
#include "constants.h"
#include <cmath>

SineTable::SineTable(int n) : ValuedTable(n, 0, PI2, sin) {};