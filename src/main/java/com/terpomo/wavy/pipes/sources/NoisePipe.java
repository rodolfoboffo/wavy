package com.terpomo.wavy.pipes.sources;

import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;
import com.terpomo.wavy.pipes.AbstractSignalSourcePipe;
import com.terpomo.wavy.signals.Noise;

public class NoisePipe extends AbstractSignalSourcePipe<Noise> {

	public NoisePipe() {
		super(new Noise());
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_AMPLITUDE)
	public void setAmplitude(float v) {
		this.signal.setAmplitude(v);
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_AMPLITUDE)
	public float getAmplitude() {
		return this.signal.getAmplitude();
	}

	@MarshalAttr(attrName=MarshallingKeys.KEY_SAMPLE_RATE)
	public void setSampleRate(int s) {
		this.signal.setSampleRate(s);
	}

	@MarshalAttr(attrName=MarshallingKeys.KEY_SAMPLE_RATE)
	public int getSampleRate() {
		return this.signal.getSampleRate();
	}


}
