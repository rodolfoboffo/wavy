package com.terpomo.wavy.math;

public class SineTable {
	public static final SineTable DEFAULT_SINE_TABLE = new SineTable(200000);
	
	private final int n;
	private final float sineValues[];
	
	public SineTable(int n) {
		this.n = n;
		this.sineValues = this.generateSineTable(n);
	}
	
	private final float[] generateSineTable(int n) {
		final float values[] = new float[n];
		for (int i = 0; i < n; i++) {
			values[i] = (float) Math.sin(MathConstants.PI2 / n * i);
		}
		return values;
	}

	public final int getLength() {
		return n;
	}

	public final float[] getValues() {
		return sineValues;
	}
	
	public final float getSineValue(int index) {
		int i = index % this.n;
		return this.sineValues[i];
	}
	
	public final float getSineValue(float rad) {
		int i = (int) (rad % MathConstants.PI2 / MathConstants.PI2 * this.n);
		return this.sineValues[i];
	}
	
}
