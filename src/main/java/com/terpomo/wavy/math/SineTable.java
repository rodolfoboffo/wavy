package com.terpomo.wavy.math;

import java.util.function.Function;

public class SineTable extends ValuedTable {
	public static final SineTable DEFAULT_SINE_TABLE = new SineTable(10240000);

	public SineTable(int n) {
		super(n, 0f, MathConstants.PI2, new Function<Float, Float>() {
			@Override
			public Float apply(Float aFloat) {
				return (float)Math.sin(aFloat);
			}
		});
	}
}
