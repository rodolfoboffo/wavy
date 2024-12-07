package com.terpomo.wavy.flow;

import com.terpomo.wavy.IWavyDisposable;
import com.terpomo.wavy.core.IObservableObject;
import com.terpomo.wavy.core.IWavyModel;
import com.terpomo.wavy.core.ObservableObject;

public class Buffer extends ObservableObject implements IWavyModel, IObservableObject, IWavyDisposable {

    public static final int DEFAULT_DATASTREAM_BUFER_SIZE = 10240;
    private final boolean endless;
    private Float[] buffer;
    private int capacity;
    private int startIndex, finishIndex;

    public Buffer(int capacity, boolean endless) {
        this.capacity = capacity;
        this.endless = endless;
        this.buffer = new Float[this.capacity+1];
        this.startIndex = this.finishIndex = 0;
    }

    public boolean isEndless() {
        return endless;
    }

    public Buffer(int capacity) {
        this(capacity, false);
    }

    public Buffer() {
        this(DEFAULT_DATASTREAM_BUFER_SIZE, false);
    }

    synchronized public void resizeBuffer(int newCapacity) {
        if (this.capacity != newCapacity) {
            Float[] newBuffer = new Float[newCapacity + 1];
            int currentSize = this.getSize();
            int n = Math.min(currentSize, newCapacity);
            for (int i = 0; i < n; i++) {
                newBuffer[n-i-1] = this.getValue(currentSize-i-1);
            }
            this.startIndex = 0;
            this.finishIndex = n;
            this.buffer = newBuffer;
            this.capacity = newCapacity;
        }
    }

    synchronized public int getSize() {
        return (this.finishIndex - this.startIndex + this.buffer.length) % this.buffer.length;
    }

    synchronized public boolean isEmpty() {
        return this.getSize() == 0;
    }

    synchronized public int getCapacity() {
        return capacity;
    }

    synchronized public int getRemainingCapacity() {
        return this.getCapacity() - this.getSize();
    }

    synchronized public Float pickOne() {
        Float v = this.buffer[this.startIndex];
        this.startIndex = (this.startIndex + 1 + this.buffer.length) % this.buffer.length;
        return v;
    }

    synchronized public Float[] fetchAll() {
        Float[] cloneBuffer = this.fetch(this.getSize());
        return cloneBuffer;
    }

    synchronized public Float[] getAll() {
        Float[] cloneBuffer = this.getValues(this.getSize());
        return cloneBuffer;
    }

    synchronized public Float[] getValues(int count) {
        int minIndex = Math.min(count, this.getSize());
        Float[] subList = new Float[minIndex];
        for (int i = 0; i < minIndex; i++) {
            subList[i] = this.buffer[(this.startIndex + i) % this.buffer.length];
        }
        return subList;
    }

    synchronized public Float getValue(int index) {
        if (index >= this.getSize())
            throw new RuntimeException("Index out of buffer.");
        return this.buffer[(this.startIndex + index) % this.buffer.length];
    }

    synchronized public Float[] fetch(int count) {
        int minIndex = Math.min(count, this.getSize());
        Float[] subList = new Float[minIndex];
        for (int i = 0; i < minIndex; i++) {
            subList[i] = this.pickOne();
        }
        return subList;
    }

    synchronized public void discard(int count) {
        int minIndex = Math.min(count, this.getSize());
        this.startIndex = (this.startIndex + minIndex) % this.buffer.length;
    }

    synchronized public boolean isFull() {
        return this.finishIndex == (this.startIndex - 1 + this.buffer.length) % this.buffer.length;
    }

    synchronized public void put(Float value) {
        boolean _isFull = this.isFull();
        if (!this.endless) {
            if (_isFull)
                throw new RuntimeException("Buffer is full.");
            this.buffer[this.finishIndex] = value;
            this.finishIndex = (this.finishIndex + 1) % this.buffer.length;
        } else {
            this.buffer[this.finishIndex] = value;
            this.finishIndex = (this.finishIndex + 1) % this.buffer.length;
            if (_isFull) {
                this.startIndex = (this.startIndex + 1) % this.buffer.length;
            }
        }
    }

    synchronized public void putAll(Float[] values) {
        int minIndex = values.length;
        for (int i = 0; i < minIndex; i++) {
            this.put(values[i]);
        }
    }

    synchronized public void clear() {
        this.startIndex = this.finishIndex = 0;
    }

    @Override
    public void wavyDispose() {

    }
}
