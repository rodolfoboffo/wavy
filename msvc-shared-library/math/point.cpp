#include "point.h"

Point::Point() : Point::Point(0.0f, 0.0f) {};

Point::Point(float x, float y) {
	this->x = x;
	this->y = y;
}

void Point::setX(float x) {
	this->x = x;
}

void Point::setY(float y) {
	this->y = y;
}

float Point::getX() {
	return this->x;
}

float Point::getY() {
	return this->y;
}

Point Point::zero() {
	return Point(0.0f, 0.0f);
}