package com.terpomo.wavy.signals;

import java.util.ArrayList;
import java.util.List;

public class Noise extends Signal {

	public static final float DEFAULT_AMPLITUDE = 0.1f;
	private float amplitude;
	private final List<Float> historicValues;

	public Noise(float amplitude, int sampleRate) {
		super(sampleRate);
		this.amplitude = amplitude;
		this.historicValues = new ArrayList<>();
	}

	public Noise() {
		this(DEFAULT_AMPLITUDE, DEFAULT_SAMPLE_RATE);
	}

	@Override
	public synchronized float getValue(long index) {
		while (this.historicValues.size() < index)
			this.getNextValue();
		return this.historicValues.get((int) index);
	}

	private float getRandomValue() {
		return (float) (Math.random() * 2.0 - 1.0) * this.amplitude;
	}

	@Override
	public synchronized float getNextValue() {
		float newValue = this.getRandomValue();
		this.historicValues.add(newValue);
		return newValue;
	}

	public synchronized void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}

	public float getAmplitude() {
		return amplitude;
	}
}
