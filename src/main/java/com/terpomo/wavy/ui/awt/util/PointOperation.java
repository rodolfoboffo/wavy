package com.terpomo.wavy.ui.awt.util;

import java.awt.Point;

public class PointOperation {
	
	public static Point sum(Point a, Point b) {
		return new Point(a.x+b.x, a.y+b.y);
	}
	
	public static Point sub(Point a, Point b) {
		return new Point(a.x-b.x, a.y-b.y);
	}
	
}
