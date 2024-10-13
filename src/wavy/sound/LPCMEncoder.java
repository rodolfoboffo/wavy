package wavy.sound;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;

import wavy.signals.Signal;

public class LPCMEncoder extends Encoder {

	
	protected final int bitsPerSample;
	protected long index = 0;
	protected boolean signed;
	protected boolean bigEndian = false;

	public LPCMEncoder(int sampleRate, int bitsPerSample, boolean signed) {
		super(sampleRate);
		this.bitsPerSample = bitsPerSample;
		this.signed = signed;
	}
	
	public LPCMEncoder(int sampleRate, int bitsPerSample) {
		this(sampleRate, bitsPerSample, DEFAULT_SIGNED);
	}
	
	public LPCMEncoder(int bitsPerSample, Signal...s) {
		this(bitsPerSample, DEFAULT_SIGNED, s);
	}
	
	public LPCMEncoder(Signal...s) {
		this(DEFAULT_BITS_PER_SAMPLE, DEFAULT_SIGNED, s);
	}
	
	public LPCMEncoder() {
		this(DEFAULT_BITS_PER_SAMPLE);
	}
	
	public LPCMEncoder(int bitsPerSample) {
		this(bitsPerSample, DEFAULT_SIGNED);
	}
	
	public LPCMEncoder(int bitsPerSample, boolean signed, Signal...s) {
		super(s);
		this.signed = signed;
		this.bitsPerSample = bitsPerSample;
	}
	
	@Override
	public AudioFormat getAudioFormat() {
		return new AudioFormat(this.sampleRate, this.bitsPerSample, this.signals.size(), this.signed, this.bigEndian);
	}
	
	@Override
	public byte[] getNumOfFrames(int n) {
		int bitsPerFrame = this.signals.size()*this.bitsPerSample;
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
			for (Signal s : this.signals) {
				frameInt <<= this.bitsPerSample;
//				float v = s.getValue(this.index);
				float v = s.getNextValue();
				int sampleInt = (int) ((v / s.getLimit()) * SIGNED_MAX);
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
