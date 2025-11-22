package com.terpomo.wavy.flow;

public class SignalBuffer extends GenericBuffer<Float> {

    public SignalBuffer(int capacity, boolean endless) {
        super(Float.class, capacity, endless);
    }

    public SignalBuffer(int capacity) {
        super(Float.class, capacity);
    }

    public SignalBuffer(boolean endless) {
        super(Float.class, endless);
    }

    public SignalBuffer() {
        super(Float.class);
    }
}
