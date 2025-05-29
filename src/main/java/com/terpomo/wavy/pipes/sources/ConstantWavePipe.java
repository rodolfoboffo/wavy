package com.terpomo.wavy.pipes.sources;

import com.terpomo.wavy.pipes.AbstractSignalSourcePipe;
import com.terpomo.wavy.signals.ConstantWave;

public class ConstantWavePipe extends AbstractSignalSourcePipe<ConstantWave> {

	public ConstantWavePipe(String pipeName) {
		this(new ConstantWave(), pipeName);
	}

	public ConstantWavePipe(ConstantWave cw, String pipeName) {
		super(cw, pipeName);
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
