package com.terpomo.wavy.oscilloscope;

public class TimeValuePair {

    private final float time;
    private final float value;

    public TimeValuePair(float time, float value) {
        this.time = time;
        this.value = value;
    }

    public float getTimeInMillisec() {
        return time;
    }

    public float getValue() {
        return value;
    }

}
