package com.terpomo.wavy.sound;

import com.terpomo.wavy.flow.Buffer;

import javax.sound.sampled.AudioFormat;

public abstract class Codec {

	public static final int DEFAULT_BITS_PER_SAMPLE = 8;
	public static final boolean DEFAULT_SIGNED = false;
	public static final boolean DEFAULT_BIG_ENDIAN = false;
	private final int sampleRate;
	private final int numOfChannels;
	private final Buffer[] buffers;
	
	public Codec(int sampleRate, Buffer[] buffers) {
		this.sampleRate = sampleRate;
		this.buffers = buffers;
		this.numOfChannels = buffers.length;
	}
	
	public abstract AudioFormat getAudioFormat();

	public int getNumOfChannels() {
		return numOfChannels;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public Buffer[] getBuffers() {
		return buffers;
	}

	public abstract byte[] encode(int n);

	public abstract Float[][] decode(byte[] readBytes, int numBytesRead);
}
