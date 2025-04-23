package com.terpomo.wavy.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class RandomAccessFileUtils {

    private static final byte[] BUFFER = new byte[Long.BYTES];
    private static final byte[] AUX_BUFFER = new byte[Long.BYTES];

    public static void writeShortReverse(short value, RandomAccessFile file) throws IOException {
        ByteBuffer.wrap(BUFFER).putShort(value);
        RandomAccessFileUtils.writeReverse(BUFFER, Short.BYTES, file);
    }

    public static void writeIntReverse(int value, RandomAccessFile file) throws IOException {
        ByteBuffer.wrap(BUFFER).putInt(value);
        RandomAccessFileUtils.writeReverse(BUFFER, Integer.BYTES, file);
    }

    public static void writeReverse(byte[] b, int length, RandomAccessFile file) throws IOException {
        for (int i = 0; i < length; i++) {
            AUX_BUFFER[i] = b[length-1-i];
        }
        file.write(AUX_BUFFER, 0, length);
    }
}
