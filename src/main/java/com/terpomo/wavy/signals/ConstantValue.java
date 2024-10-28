package com.terpomo.wavy.signals;

public class ConstantValue extends Signal {

	protected float value;
	
	public ConstantValue(float value, int sampleRate) {
		super(sampleRate);
		this.value = value;
	}

	@Override
	public float getValue(long index) {
		return value;
	}
	
	@Override
	public float getNextValue() {
		return value;
	}
}
