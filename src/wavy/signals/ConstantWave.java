package wavy.signals;

import wavy.Configuration;
import wavy.Constants;
import wavy.math.SineTable;

public class ConstantWave extends Signal {

	protected float frequency;
	protected float amplitude;
	protected float phase;
	protected SineTable sineTable;

	public ConstantWave(int sampleRate, float frequency, float amplitude, SineTable sineTable) {
		super(sampleRate);
		this.frequency = frequency;
		this.amplitude = amplitude;
		this.sineTable = sineTable;
		this.phase = 0.0f;
	}
	
	public ConstantWave(int sampleRate, float frequency) {
		this(sampleRate, frequency, 1.0f, Configuration.SINE_TABLE);
	}

	public final float getFrequency() {
		return frequency;
	}
	
	public final float getAmplitude() {
		return amplitude;
	}

	@Override
	public float getValue(long index) {
		float frac = (float) index / this.sampleRate * Constants.PI2 * this.frequency;
		float value = this.amplitude * this.sineTable.getSineValue(frac);
		return this.clamp(value);
	}
	
	public float getNextValue() {
		this.phase = 1.0f / this.sampleRate * Constants.PI2 * this.frequency + this.phase;
		float value = this.amplitude * this.sineTable.getSineValue(this.phase);
		return this.clamp(value);
	}
}
