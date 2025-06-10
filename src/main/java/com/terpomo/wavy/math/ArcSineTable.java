package com.terpomo.wavy.math;

public class ArcSineTable extends ValuedTable {
	public static final ArcSineTable DEFAULT_ARC_SINE_TABLE = new ArcSineTable(10240000);

	public ArcSineTable(int n) {
		super(n, -1f, 1f, aFloat -> (float)Math.asin(aFloat));
	}

}
