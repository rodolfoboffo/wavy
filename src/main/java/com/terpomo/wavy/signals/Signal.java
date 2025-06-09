package com.terpomo.wavy.signals;

import com.terpomo.wavy.Constants;

public abstract class Signal {

	public static final int DEFAULT_SAMPLE_RATE = Constants.DEFAULT_SAMPLE_RATE;
	protected int sampleRate;
	protected float limit = 1.0f;

	public Signal(int sampleRate) {
		super();
		this.sampleRate = sampleRate;
	}

	public Signal() {
		this(DEFAULT_SAMPLE_RATE);
	}

	public final int getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	public float getValue(long index) {
		return 0;
	}
	
	public float getNextValue() {
		return 0;
	}
	
	public final float getLimit() {
		return limit;
	}

	public final void setLimit(float limit) {
		this.limit = limit;
	}

	protected float clamp(float value) {
		if (Math.abs(value) > this.limit) {
			return value > 0 ? this.limit : -this.limit;
		}
		return value;
	}
	
}
