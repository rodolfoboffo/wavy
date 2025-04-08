package com.terpomo.wavy.sound;

import com.terpomo.wavy.flow.Buffer;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LPCMEncoder extends Encoder {

	protected long index = 0;
	
	public LPCMEncoder(int sampleRate, Buffer[] buffers) {
		super(sampleRate, DEFAULT_BITS_PER_SAMPLE, DEFAULT_SIGNED, DEFAULT_BIG_ENDIAN, buffers);
	}

	@Override
	public AudioFormat getAudioFormat() {
		return new AudioFormat(this.getSampleRate(), this.bitsPerSample, this.getBuffers().length, this.signed, this.bigEndian);
	}
	
	@Override
	public byte[] encode (int numOfFrames, Buffer[] buffers) {
		int bitsPerFrame = this.getBuffers().length*this.bitsPerSample;
		if (bitsPerFrame % 8 != 0)
			throw new RuntimeException("Number of bits per frame should be multiple of 8.");
		int bytesPerFrame = bitsPerFrame / 8;
		byte[] bytes = new byte[numOfFrames * bytesPerFrame];
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		if (!this.bigEndian)
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		int frameInt;
		int SIGNED_MAX = (1 << this.bitsPerSample) - 1;
		for (int iFrame = 0; iFrame < numOfFrames; iFrame++) {
			frameInt = 0;
			buffer.clear();
			for (Buffer b : this.getBuffers()) {
				frameInt <<= this.bitsPerSample;
				float v = b.pickOne();
				int sampleInt = (int) ((v / 2.0f * SIGNED_MAX) % SIGNED_MAX);
				if (!this.signed)
					sampleInt += SIGNED_MAX/2;
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
