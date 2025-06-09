package com.terpomo.wavy.signals;

public class ConstantValue extends Signal {

	public static final float DEFAULT_CONSTANT_VALUE = 1.0f;
	protected float constantValue;
	
	public ConstantValue(float value, int sampleRate) {
		super(sampleRate);
		this.constantValue = value;
	}

	public ConstantValue(float value) {
		this(value, DEFAULT_SAMPLE_RATE);
	}

	public ConstantValue() {
		this(DEFAULT_CONSTANT_VALUE, DEFAULT_SAMPLE_RATE);
	}

	@Override
	public float getValue(long index) {
		return constantValue;
	}
	
	@Override
	public float getNextValue() {
		return constantValue;
	}

	public synchronized void setConstantValue(float constantValue) {
		this.constantValue = constantValue;
	}

	public float getConstantValue() {
		return constantValue;
	}
}
