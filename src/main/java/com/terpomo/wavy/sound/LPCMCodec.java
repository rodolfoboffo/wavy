package com.terpomo.wavy.sound;

import com.terpomo.wavy.flow.Buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;

public class LPCMCodec extends Codec {

	protected final int bitsPerSample;
	protected long index = 0;
	protected boolean signed;
	protected boolean bigEndian;

	public LPCMCodec(int sampleRate, int bitsPerSample, boolean signed, boolean bigEndian, Buffer[] buffers) {
		super(sampleRate, buffers);
		this.bitsPerSample = bitsPerSample;
		this.signed = signed;
		this.bigEndian = bigEndian;
	}
	
	public LPCMCodec(int sampleRate, int bitsPerSample, Buffer[] buffers) {
		this(sampleRate, bitsPerSample, DEFAULT_SIGNED, DEFAULT_BIG_ENDIAN, buffers);
	}
	
	public LPCMCodec(int sampleRate, Buffer[] buffers) {
		this(sampleRate, DEFAULT_BITS_PER_SAMPLE, DEFAULT_SIGNED, DEFAULT_BIG_ENDIAN, buffers);
	}

	@Override
	public AudioFormat getAudioFormat() {
		return new AudioFormat(this.getSampleRate(), this.bitsPerSample, this.getBuffers().length, this.signed, this.bigEndian);
	}
	
	@Override
	public byte[] encode (int numOfFrames) {
		int bitsPerFrame = this.getBuffers().length*this.bitsPerSample;
		if (bitsPerFrame % 8 != 0)
			throw new RuntimeException("Number of bits per frame should be multiple of 8.");
		int bytesPerFrame = bitsPerFrame / 8;
		byte[] bytes = new byte[numOfFrames * bytesPerFrame];
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		if (!this.bigEndian)
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		int frameInt;
		int SIGNED_MAX = 1 << this.bitsPerSample - 1;
		for (int iFrame = 0; iFrame < numOfFrames; iFrame++) {
			frameInt = 0;
			buffer.clear();
			for (Buffer b : this.getBuffers()) {
				frameInt <<= this.bitsPerSample;
				float v = b.pickOne();
				int sampleInt = (int) ((v * SIGNED_MAX) % SIGNED_MAX);
				if (!this.signed)
					sampleInt += SIGNED_MAX;
				frameInt |= sampleInt;
			}
			buffer.putInt(frameInt);
			for (int iByte = 0; iByte < bytesPerFrame; iByte++) {
				bytes[iByte+iFrame*bytesPerFrame] = buffer.get(iByte);
			}
			this.index++;
		}
		return bytes;
	}

	@Override
	public Float[][] decode(byte[] readBytes, int numBytesRead) {
		if (this.bigEndian)
			throw new RuntimeException("Endianess not expected.");
		int numChannels = getNumOfChannels();
		long sampleMask = (1L << this.bitsPerSample) - 1;
		int SIGNED_MAX = (1 << this.bitsPerSample) - 1;
		int bitsPerFrame = this.bitsPerSample * numChannels;
		int bytesPerFrame = bitsPerFrame / 8;
		int numFramesRead = numBytesRead * 8 / bitsPerFrame;
		Float[][] valuesByChannel = new Float[numChannels][numFramesRead];
		for (int iFrame = 0; iFrame < numFramesRead; iFrame++) {
			long longFrame = 0L;
			for (int z = 0; z < bytesPerFrame; z++) {
				longFrame = longFrame << 8;
				longFrame = longFrame | (readBytes[(bytesPerFrame-z-1)+bytesPerFrame*iFrame]);
			}
			for (int jChannel = 0; jChannel < numChannels; jChannel++) {
				int intSample = (int)(longFrame & sampleMask);
				longFrame = longFrame >> this.bitsPerSample;
				if (!this.signed)
					intSample -= SIGNED_MAX;
				float channelValue = 1.0f * intSample / SIGNED_MAX;
				valuesByChannel[jChannel][iFrame] = channelValue;
			}
		}
		return valuesByChannel;
	}
}
