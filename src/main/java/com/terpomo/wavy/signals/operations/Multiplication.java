package com.terpomo.wavy.signals.operations;

import com.terpomo.wavy.signals.Signal;

public class Multiplication extends BinaryOp {

	public Multiplication(Signal s1, Signal s2) {
		super(s1, s2);
	}

	@Override
	public float getNextValue() {
		return signal1.getNextValue() * signal2.getNextValue();
	}
	
	@Override
	public float getValue(long index) {
		return signal1.getValue(index) * signal2.getValue(index);
	}
	
}
