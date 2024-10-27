package com.terpomo.wavy.ui.awt;

import java.awt.Point;

public class PointOp {
	
	public static Point sum(Point a, Point b) {
		return new Point(a.x+b.x, a.y+b.y);
	}
	
	public static Point sub(Point a, Point b) {
		return new Point(a.x-b.x, a.y-b.y);
	}
	
}
