package com.terpomo.wavy.util;

import com.terpomo.wavy.marshal.IMarshallable;
import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;

public class Dimension  implements IMarshallable {
    private float width, height;

    public Dimension() {
    }

    public Dimension(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_HEIGHT)
    public void setHeight(float height) {
        this.height = height;
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_WIDTH)
    public void setWidth(float width) {
        this.width = width;
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_WIDTH)
    public float getWidth() {
        return width;
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_HEIGHT)
    public float getHeight() {
        return height;
    }
}
