package com.terpomo.wavy.math;

import java.util.function.Function;

public class ArcSineTable extends ValuedTable {
	public static final ArcSineTable DEFAULT_ARC_SINE_TABLE = new ArcSineTable(10240000);

	public ArcSineTable(int n) {
		super(n, -1f, 1f, new Function<Float, Float>() {
			@Override
			public Float apply(Float aFloat) {
				return (float)Math.asin(aFloat);
			}
		});
	}
}
