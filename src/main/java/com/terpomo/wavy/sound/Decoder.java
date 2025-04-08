package com.terpomo.wavy.sound;

import com.terpomo.wavy.flow.Buffer;

import javax.sound.sampled.AudioFormat;

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
