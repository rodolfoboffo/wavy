package com.terpomo.wavy.oscilloscope;

public class TimeValuePair {

    private final float time;
    private final Float value;

    public TimeValuePair(float time, Float value) {
        this.time = time;
        this.value = value;
    }

    public float getTime() {
        return time;
    }

    public Float getValue() {
        return value;
    }

}
