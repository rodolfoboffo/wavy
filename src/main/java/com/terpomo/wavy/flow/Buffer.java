package com.terpomo.wavy.flow;

import java.util.ArrayList;
import java.util.List;

public class Buffer {

    public static final int DEFAULT_DATASTREAM_BUFER_SIZE = 10240;
    protected List<Float> buffer;
    protected final int capacity;

    public Buffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new ArrayList<>(this.capacity);
    }

    public Buffer() {
        this(DEFAULT_DATASTREAM_BUFER_SIZE);
    }

    synchronized public int getSize() {
        return this.buffer.size();
    }

    synchronized public int getCapacity() {
        return capacity;
    }

    synchronized public int getRemainingCapacity() {
        return this.capacity - this.buffer.size();
    }

    synchronized public Float pick() {
        Float v = this.buffer.get(0);
        this.buffer.remove(0);
        return v;
    }

    synchronized public List<Float> fetchAll() {
        List<Float> cloneBuffer = new ArrayList<>(this.buffer);
        this.buffer.clear();
        return cloneBuffer;
    }

    synchronized public List<Float> getAll() {
        List<Float> cloneBuffer = new ArrayList<>(this.buffer);
        return cloneBuffer;
    }

    synchronized public List<Float> fetch(int count) {
        List<Float> subList = this.buffer.subList(0, Math.min(count, this.buffer.size()));
        List<Float> cloneBuffer = new ArrayList<>(subList);
        this.buffer.removeAll(subList);
        return cloneBuffer;
    }

    synchronized public void put(Float value) {
        this.buffer.add(value);
    }

    synchronized public void putAll(List<Float> values) {
        this.buffer.addAll(values);
    }
}
