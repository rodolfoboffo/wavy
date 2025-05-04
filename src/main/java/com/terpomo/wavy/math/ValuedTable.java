package com.terpomo.wavy.math;

import java.util.function.Function;

public class ValuedTable {

	private final int n;
	private final float values[];
	private final float lowerBoundary;
	private final float upperBoundary;
	private final Function<Float, Float> function;
	private final boolean isCyclical;
	private final float intervalLength;

	public ValuedTable(int n, float lowerBoundary, float upperBoundary, Function<Float, Float> f) {
		this(n, lowerBoundary, upperBoundary, f, true);
	}

	public ValuedTable(int n, float lowerBoundary, float upperBoundary, Function<Float, Float> f, boolean isCyclical) {
		this.n = n;
		this.upperBoundary = Math.max(upperBoundary, lowerBoundary);
		this.lowerBoundary = Math.min(upperBoundary, lowerBoundary);
		this.intervalLength = (this.upperBoundary - this.lowerBoundary);
		this.function = f;
		this.isCyclical = isCyclical;
		this.values = this.generateTable();
	}
	
	private float[] generateTable() {
		final float values[] = new float[this.n];
		for (int i = 0; i < this.n; i++) {
			values[i] = this.function.apply((this.upperBoundary - this.lowerBoundary) / n * i + this.lowerBoundary);
		}
		return values;
	}

	public final int getLength() {
		return n;
	}

	public final float[] getValues() {
		return values;
	}
	
	public final float getValue(int index) {
		if (!this.isCyclical && (index < 0 || index >= n))
			throw new RuntimeException("Valued Table out of boundaries.");
		int i = index % this.n;
		return this.values[i];
	}
	
	public final float getValue(float domainValue) {
		if (!this.isCyclical && (domainValue < this.lowerBoundary || domainValue > this.upperBoundary))
			throw new RuntimeException("Valued Table out of boundaries.");
		float v = domainValue % (this.intervalLength) / (this.intervalLength);
		v = v >= 0 ? v : 1 + v;
		int i = (int) (v * this.n);
		return this.values[i];
	}
	
}
