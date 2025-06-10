package com.terpomo.wavy.util;

import com.terpomo.wavy.marshal.IMarshallable;
import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;

public class Point implements IMarshallable {
    private float x, y;

    public Point() {
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_X)
    public void setX(float x) {
        this.x = x;
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_Y)
    public void setY(float y) {
        this.y = y;
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_X)
    public float getX() {
        return x;
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_Y)
    public float getY() {
        return y;
    }
}
