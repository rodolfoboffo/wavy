#pragma once

class Point {
private:
	float x, y;
public:
	Point();
	Point(float x, float y);
	void setX(float x);
	void setY(float y);
	float getX();
	float getY();
	static Point zero();
};