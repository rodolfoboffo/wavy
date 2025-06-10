package com.terpomo.wavy.sound;

public abstract class AbstractCodec {
    public static final int DEFAULT_BITS_PER_SAMPLE = 8;
    public static final boolean DEFAULT_SIGNED = false;
    public static final boolean DEFAULT_BIG_ENDIAN = false;

    protected final int sampleRate;
    protected final int bitsPerSample;
    protected final boolean signed;
    protected final boolean bigEndian;

    protected AbstractCodec(int sampleRate, int bitsPerSample, boolean signed, boolean bigEndian) {
        this.sampleRate = sampleRate;
        this.bitsPerSample = bitsPerSample;
        this.signed = signed;
        this.bigEndian = bigEndian;
    }

    public int getSampleRate() {
        return sampleRate;
    }
}
