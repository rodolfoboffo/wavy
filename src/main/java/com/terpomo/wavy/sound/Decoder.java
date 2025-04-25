package com.terpomo.wavy.sound;

public abstract class Decoder extends AbstractCodec {

	protected int numOfChannels;

	protected Decoder(int sampleRate, int bitsPerSample, boolean signed, boolean bigEndian, int numOfChannels) {
		super(sampleRate, bitsPerSample, signed, bigEndian);
		this.numOfChannels = numOfChannels;
	}

	public int getNumOfChannels() {
		return numOfChannels;
	}

	public abstract Float[][] decode(byte[] readBytes, int numBytesRead);
}
