package com.terpomo.wavy.signals;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.math.MathConstants;
import com.terpomo.wavy.math.SineTable;

public class ConstantWave extends Signal {

	public static final int DEFAULT_CW_FREQUENCY = 400; 
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
	
	public ConstantWave() {
		this(Constants.DEFAULT_SAMPLE_RATE, DEFAULT_CW_FREQUENCY, 1.0f, SineTable.DEFAULT_SINE_TABLE);
	}
	
	public ConstantWave(int sampleRate, float frequency) {
		this(sampleRate, frequency, 1.0f, SineTable.DEFAULT_SINE_TABLE);
	}

	public final float getFrequency() {
		return frequency;
	}
	
	public final float getAmplitude() {
		return amplitude;
	}

	@Override
	public float getValue(long index) {
		float frac = (float) index / this.sampleRate * MathConstants.PI2 * this.frequency % MathConstants.PI2;
		float value = this.amplitude * this.sineTable.getSineValue(frac);
		return this.clamp(value);
	}
	
	public float getNextValue() {
		this.phase = (1.0f / this.sampleRate * MathConstants.PI2 * this.frequency + this.phase) % MathConstants.PI2;
		float value = this.amplitude * this.sineTable.getSineValue(this.phase);
		return this.clamp(value);
	}
}
