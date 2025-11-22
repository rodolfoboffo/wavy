package com.terpomo.wavy.sound;

public class LPCMDecoder extends Decoder {

	public LPCMDecoder(int sampleRate, int bitsPerSample, boolean signed, boolean bigEndian, int numOfChannels) {
		super(sampleRate, bitsPerSample, signed, bigEndian, numOfChannels);
	}

	@Override
	public Float[][] decode(byte[] readBytes, int numBytesRead) {
		if (this.bigEndian)
			throw new RuntimeException("Endianess not expected.");
		int numChannels = getNumOfChannels();
		long sampleMask = (1L << this.bitsPerSample) - 1;
		long byteMask = ((1<<8)-1);
		int SIGNED_MAX = (1 << this.bitsPerSample) - 1;
		int bitsPerFrame = this.bitsPerSample * numChannels;
		int bytesPerFrame = bitsPerFrame / 8;
		int numFramesRead = numBytesRead * 8 / bitsPerFrame;
		Float[][] valuesByChannel = new Float[numChannels][numFramesRead];
		for (int iFrame = 0; iFrame < numFramesRead; iFrame++) {
			long longFrame = 0L;
			for (int z = 1; z <= bytesPerFrame; z++) {
				longFrame = longFrame << 8;
				longFrame = longFrame | (byteMask & readBytes[bytesPerFrame*(iFrame+1)-z]);
			}
			for (int jChannel = 0; jChannel < numChannels; jChannel++) {
				int intSample = ((int)(longFrame & sampleMask) + SIGNED_MAX) % SIGNED_MAX;
				longFrame = longFrame >> this.bitsPerSample;
				float channelValue = 2.0f * intSample / SIGNED_MAX - 1.0f;
				valuesByChannel[jChannel][iFrame] = channelValue;
			}
		}
		return valuesByChannel;
	}
}
