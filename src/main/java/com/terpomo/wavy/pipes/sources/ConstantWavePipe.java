package com.terpomo.wavy.pipes.sources;

import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;
import com.terpomo.wavy.math.ExpressionEvaluator;
import com.terpomo.wavy.pipes.AbstractSignalSourcePipe;
import com.terpomo.wavy.signals.ConstantWave;

public class ConstantWavePipe extends AbstractSignalSourcePipe<ConstantWave> {

	public static final String DEFAULT_PHASE = "0";
	private String phase;

	public ConstantWavePipe() {
		this(new ConstantWave());
		this.setPhase(DEFAULT_PHASE);
	}

	public ConstantWavePipe(ConstantWave cw) {
		super(cw);
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_FREQUENCY)
	public void setFrequency(float v) {
		this.signal.setFrequency(v);
	}

	@MarshalAttr(attrName=MarshallingKeys.KEY_SAMPLE_RATE)
	public void setSampleRate(int s) {
		this.signal.setSampleRate(s);
	}

	@MarshalAttr(attrName=MarshallingKeys.KEY_AMPLITUDE)
	public void setAmplitude(float a) {
		this.signal.setAmplitude(a);
	}

	@MarshalAttr(attrName=MarshallingKeys.KEY_AMPLITUDE)
	public float getAmplitude() {
		return this.signal.getAmplitude();
	}

	@MarshalAttr(attrName=MarshallingKeys.KEY_SAMPLE_RATE)
	public int getSampleRate() {
		return this.signal.getSampleRate();
	}

	@MarshalAttr(attrName=MarshallingKeys.KEY_FREQUENCY)
	public float getFrequency() {
		return this.signal.getFrequency();
	}

	@MarshalAttr(attrName=MarshallingKeys.KEY_PHASE)
	public String getPhase() {
		return phase;
	}

	@MarshalAttr(attrName=MarshallingKeys.KEY_PHASE)
	public synchronized void setPhase(String phase) {
		try {
			if (phase == null || phase.isEmpty())
				phase = DEFAULT_PHASE;
			float floatPhase = ExpressionEvaluator.evaluate(phase);
			this.phase = phase;
			this.signal.setInitialPhase(floatPhase);
		} catch (Exception e) {
            throw new RuntimeException(String.format("Could not parse expression %s.", phase), e);
        }
    }
}
