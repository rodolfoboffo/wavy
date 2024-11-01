package com.terpomo.wavy.flow;

import com.terpomo.wavy.signals.ConstantWave;
import com.terpomo.wavy.signals.ConstantWavePipe;

public class PipeFactory {
	
	public static AbstractPipe createPipe(PipeTypeEnum pipeType) {
		switch (pipeType) {
		case CONSTANT_WAVE_SIGNAL_PIPE_ENUM: {
			return new ConstantWavePipe(new ConstantWave());
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + pipeType);
		}
	}
	
}
