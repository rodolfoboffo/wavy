package com.terpomo.wavy.sound;

import com.terpomo.wavy.flow.Buffer;

import java.util.Queue;

import javax.sound.sampled.AudioFormat;

public abstract class Encoder {
	
	protected static final int DEFAULT_BITS_PER_SAMPLE = 8;
	protected static final boolean DEFAULT_SIGNED = false;
	protected final int sampleRate;
	protected Buffer[] buffers;
	
	public Encoder(int sampleRate, Buffer[] buffers) {
		this.sampleRate = sampleRate;
		this.buffers = buffers;
	}
	
	public abstract AudioFormat getAudioFormat();
	
	public final int getSampleRate() {
		return sampleRate;
	}

	public abstract byte[] getNumOfFrames(int n);
}
