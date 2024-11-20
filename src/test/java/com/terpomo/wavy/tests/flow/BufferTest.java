package com.terpomo.wavy.tests.flow;

import com.terpomo.wavy.flow.Buffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.util.Arrays;

import static org.junit.Assert.assertThrows;

public class BufferTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testBufferGeneral() {
        Buffer buffer = new Buffer(3);
        buffer.put(1f);
        buffer.put(2f);
        Float value = buffer.pickOne();
        assert (value == 1f);
        buffer.put(3f);
        assert (buffer.getSize() == 2);
        value = buffer.pickOne();
        assert (value == 2f);
        value = buffer.pickOne();
        assert (value == 3f);
        assert (buffer.isEmpty());
    }

    @Test
    public void testBufferFull() {
        Buffer buffer = new Buffer(3);
        buffer.put(1f);
        buffer.put(2f);
        buffer.put(3f);
        assert (buffer.isFull());
        assert (buffer.getSize() == 3);
        assertThrows(RuntimeException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                buffer.put(4f);
            }
        });
        buffer.clear();
        assert (buffer.getSize() == 0);
    }

    @Test
    public void testEndlessBuffer() {
        Buffer buffer = new Buffer(3, true);
        buffer.put(1f);
        buffer.put(2f);
        buffer.put(3f);
        buffer.put(4f);
        buffer.put(5f);
        Float[] values = buffer.fetchAll();
        assert (Arrays.equals(values, new Float[] {3f, 4f, 5f}));
    }

}
