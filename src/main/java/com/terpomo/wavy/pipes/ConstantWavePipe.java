package com.terpomo.wavy.pipes;

import com.terpomo.wavy.signals.ConstantWave;

public class ConstantWavePipe extends AbstractSignalSourcePipe<ConstantWave> {

	public ConstantWavePipe(ConstantWave cw) {
		super(cw);
	}
	
	public void setFrequency(float v) {
		this.signal.setFrequency(v);
	}

	public void setSampleRate(int s) {
		this.signal.setSampleRate(s);
	}

	public void setAmplitude(float a) {
		this.signal.setAmplitude(a);
	}
}
