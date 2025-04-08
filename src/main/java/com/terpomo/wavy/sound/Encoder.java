package com.terpomo.wavy.sound;

import com.terpomo.wavy.flow.Buffer;

import javax.sound.sampled.AudioFormat;

public abstract class Encoder extends AbstractCodec {

	private final Buffer[] buffers;
	
	public Encoder(int sampleRate, int bitsPerSample, boolean signed, boolean bigEndian, Buffer[] buffers) {
		super(sampleRate, bitsPerSample, signed, bigEndian);
		this.buffers = buffers;
	}
	
	public abstract AudioFormat getAudioFormat();

	public Buffer[] getBuffers() {
		return buffers;
	}

	public abstract byte[] encode(int n, Buffer[] buffers);

}
