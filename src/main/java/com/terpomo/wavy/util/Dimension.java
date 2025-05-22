package com.terpomo.wavy.util;

import com.terpomo.wavy.marshal.IMarshallable;

public class Dimension  implements IMarshallable {
    private final float width, height;

    public Dimension(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
