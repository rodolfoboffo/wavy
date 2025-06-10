package com.terpomo.wavy.signals;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.math.MathConstants;
import com.terpomo.wavy.math.SineTable;

public class ConstantWave extends Signal {

	public static final int DEFAULT_CW_FREQUENCY = 400;
	public static final float DEFAULT_CW_AMPLITUDE = 1.0f;
	public static final int DEFAULT_CW_SAMPLE_RATE = Constants.DEFAULT_SAMPLE_RATE;
	protected float frequency;
	protected float amplitude;
	protected float phase;
	protected float initialPhase;
	protected SineTable sineTable;

	public ConstantWave(int sampleRate, float frequency, float amplitude, float initialPhase, SineTable sineTable) {
		super(sampleRate);
		this.frequency = frequency;
		this.amplitude = amplitude;
		this.sineTable = sineTable;
		this.phase = this.initialPhase = initialPhase;
	}
	
	public ConstantWave() {
		this(DEFAULT_CW_SAMPLE_RATE, DEFAULT_CW_FREQUENCY, DEFAULT_CW_AMPLITUDE, 0.0f, SineTable.DEFAULT_SINE_TABLE);
	}
	
	public ConstantWave(int sampleRate, float frequency) {
		this(sampleRate, frequency, DEFAULT_CW_AMPLITUDE, 0.0f, SineTable.DEFAULT_SINE_TABLE);
	}

	public ConstantWave(int sampleRate, float frequency, float initialPhase) {
		this(sampleRate, frequency, DEFAULT_CW_AMPLITUDE, initialPhase, SineTable.DEFAULT_SINE_TABLE);
	}

	public final float getFrequency() {
		return frequency;
	}
	
	public final float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}

	public synchronized void setInitialPhase(float initialPhase) {
		this.initialPhase = this.phase = initialPhase;
	}

	public float getInitialPhase() {
		return initialPhase;
	}

	synchronized public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	@Override
	synchronized public float getValue(long index) {
		float frac = ((float)index / this.sampleRate * MathConstants.PI2 * this.frequency + this.initialPhase) % MathConstants.PI2;
		float value = this.amplitude * this.sineTable.getValue(frac);
		return this.clamp(value);
	}
	
	synchronized public float getNextValue() {
		this.phase = (1.0f / this.sampleRate * MathConstants.PI2 * this.frequency + this.phase) % MathConstants.PI2;
		float value = this.amplitude * this.sineTable.getValue(this.phase);
		return this.clamp(value);
	}
}
