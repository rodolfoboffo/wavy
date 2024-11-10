package com.terpomo.wavy.signals;

public class ConstantWavePipe extends AbstractSignalSourcePipe<ConstantWave> {

	public ConstantWavePipe(ConstantWave cw) {
		super(cw);
	}
	
	public void setFrequency(float v) {
		this.signal.setFrequency(v);
	}

}
