package wavy;

public class SineTable {

	private final int numValues;
	private final float sineValues[];
	
	public SineTable(int n) {
		this.numValues=n;
		this.sineValues = this.generateSineTable(n);
	}
	
	private final float[] generateSineTable(int n) {
		final float values[] = new float[n];
		for (int i = 0; i < n; i++) {
			values[i] = (float) Math.sin(Constants.PI2 / n * i);
		}
		return values;
	}

	public final int getNumValues() {
		return numValues;
	}

	public final float[] getSineValues() {
		return sineValues;
	}
	
}
