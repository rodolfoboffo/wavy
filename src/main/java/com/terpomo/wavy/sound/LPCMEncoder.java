package com.terpomo.wavy.sound;

import com.terpomo.wavy.flow.Buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;

public class LPCMEncoder extends Encoder {

	
	protected final int bitsPerSample;
	protected long index = 0;
	protected boolean signed;
	protected boolean bigEndian = false;

	public LPCMEncoder(int sampleRate, int bitsPerSample, boolean signed, Buffer[] buffers) {
		super(sampleRate, buffers);
		this.bitsPerSample = bitsPerSample;
		this.signed = signed;
	}
	
	public LPCMEncoder(int sampleRate, int bitsPerSample, Buffer[] buffers) {
		this(sampleRate, bitsPerSample, DEFAULT_SIGNED, buffers);
	}
	
	public LPCMEncoder(int sampleRate, Buffer[] buffers) {
		this(sampleRate, DEFAULT_BITS_PER_SAMPLE, DEFAULT_SIGNED, buffers);
	}

	@Override
	public AudioFormat getAudioFormat() {
		return new AudioFormat(this.sampleRate, this.bitsPerSample, this.buffers.length, this.signed, this.bigEndian);
	}
	
	@Override
	public byte[] getNumOfFrames(int n) {
		int bitsPerFrame = this.buffers.length*this.bitsPerSample;
		if (bitsPerFrame % 8 != 0)
			throw new RuntimeException("Number of bits per frame should be multiple of 8.");
		int bytesPerFrame = bitsPerFrame / 8;
		byte[] bytes = new byte[n * bytesPerFrame];
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		if (!this.bigEndian)
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		Integer frameInt;
		int SIGNED_MAX = 1 << this.bitsPerSample - 1;
		for (int iFrame = 0; iFrame < n; iFrame++) {
			frameInt = 0;
			buffer.clear();
			for (Buffer b : this.buffers) {
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
}
