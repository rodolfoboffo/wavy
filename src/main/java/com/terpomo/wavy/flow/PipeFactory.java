package com.terpomo.wavy.flow;

import com.terpomo.wavy.signals.ConstantWave;
import com.terpomo.wavy.signals.ConstantWavePipe;

public class PipeFactory {

	public static final String PIPE_CW = "PIPE_CW";
	
	public static AbstractPipe createPipe(String pipeType) {
		switch (pipeType) {
		case PIPE_CW: {
			return new ConstantWavePipe(new ConstantWave());
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + pipeType);
		}
	}
	
}
