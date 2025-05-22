package com.terpomo.wavy.util;

import com.terpomo.wavy.marshal.IMarshallable;

public class Point implements IMarshallable {
    private final float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
