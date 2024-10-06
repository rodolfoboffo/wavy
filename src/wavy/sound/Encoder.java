package wavy.sound;

import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFormat;

import wavy.signals.Signal;

public abstract class Encoder {
	
	protected static final int DEFAULT_BITS_PER_SAMPLE = 8;
	protected static final boolean DEFAULT_SIGNED = false;
	protected final int sampleRate;
	protected List<Signal> signals;
	
	public Encoder(int sampleRate) {
		this.sampleRate = sampleRate;
	}
	
	public Encoder(Signal...signals) {
		this.signals = Arrays.asList(signals);
		this.sampleRate = this.getSignalsSampleRate(this.signals);
	}
	
	protected int getSignalsSampleRate(List<Signal> signals) {
		boolean sampleRateNotDefined = true;
		int sampleRate = 0;
		for (Signal s : this.signals) {
			if (sampleRateNotDefined) {
				sampleRate = s.getSampleRate();
				sampleRateNotDefined = false;
			}
			else if (s.getSampleRate() != sampleRate) {
				throw new RuntimeException("Not all Signals have the same Sample Rate.");
			}
		}
		return sampleRate;
	}
	
	public void addSignal(Signal s) {
		if (s.getSampleRate() != this.sampleRate)
			throw new RuntimeException(String.format("New signal has sample rate %d. Encoder expexts %d.", s.getSampleRate(), this.sampleRate));
		this.signals.add(s);
	}
	
	public abstract AudioFormat getAudioFormat();
	
	public final int getSampleRate() {
		return sampleRate;
	}

	public abstract byte[] getNumOfFrames(int n);
}
