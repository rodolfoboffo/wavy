package com.terpomo.wavy.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Buffer {

    public static final int DEFAULT_DATASTREAM_BUFER_SIZE = 10240;
    protected List<Float> buffer;
    protected final int capacity;

    Buffer() {
        this.capacity = DEFAULT_DATASTREAM_BUFER_SIZE;
        this.buffer = new ArrayList<>(this.capacity);
    }

    public int getSize() {
        return this.buffer.size();
    }

    public int getCapacity() {
        return capacity;
    }

    synchronized public void put(Float value) {
        this.buffer.add(value);
    }
}
