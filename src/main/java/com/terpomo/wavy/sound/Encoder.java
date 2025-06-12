package com.terpomo.wavy.sound;

import com.terpomo.wavy.flow.SignalBuffer;

import javax.sound.sampled.AudioFormat;

public abstract class Encoder extends AbstractCodec {

	private AudioFormat audioFormat;
	
	public Encoder(int sampleRate, int bitsPerSample, int numOfChannels, boolean signed, boolean bigEndian) {
		super(sampleRate, bitsPerSample, signed, bigEndian);
		this.audioFormat = new AudioFormat(sampleRate, bitsPerSample, numOfChannels, signed, bigEndian);
	}
	
	public AudioFormat getAudioFormat() {
		return this.audioFormat;
	}

	public abstract byte[] encode(int n, SignalBuffer[] buffers, float multiplier);

	public byte[] encode(int n, SignalBuffer[] buffers) {
		return this.encode(n, buffers, 1f);
	}

	public int getBytesPerFrame() {
		return this.audioFormat.getSampleSizeInBits() * this.audioFormat.getChannels() / 8;
	}

}
