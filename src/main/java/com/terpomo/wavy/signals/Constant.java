package com.terpomo.wavy.signals;

public class Constant extends Signal {

	protected float value;
	
	public Constant(float value, int sampleRate) {
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
