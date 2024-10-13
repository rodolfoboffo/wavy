package wavy.signals.operations;

import wavy.signals.Constant;
import wavy.signals.Signal;

public abstract class BinaryOp extends SignalOp {

	protected Signal signal1;
	protected Signal signal2;
		
	public BinaryOp(Signal s1, Signal s2) {
		super(s1.getSampleRate());
		if (s1.getSampleRate() != s2.getSampleRate())
			throw new RuntimeException("Binary Operation factors should have the same sample rate.");
		this.signal1 = s1;
		this.signal2 = s2;
	}
	
	public BinaryOp(float s1, Signal s2) {
		this(new Constant(s1, s2.getSampleRate()), s2);
	}
	
	public BinaryOp(Signal s1, float s2) {
		this(s1, new Constant(s2, s1.getSampleRate()));
	}

	public Signal getSignal1() {
		return signal1;
	}

	public Signal getSignal2() {
		return signal2;
	}
	
}
