package com.terpomo.wavy.ui.awt.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.PipeFactory;
import com.terpomo.wavy.flow.PipeTypeEnum;
import com.terpomo.wavy.pipes.AudioPlayerPipe;
import com.terpomo.wavy.pipes.ConstantWavePipe;
import com.terpomo.wavy.pipes.OscilloscopePipe;

public class PipeReprFactory {

	public static AbstractPipeRepr createPipeRepr(PipeTypeEnum pipeType, String pipeName) {
		AbstractPipe pipe = PipeFactory.createPipe(pipeType);
		AbstractPipeRepr pipeRepr;
		switch (pipeType) {
		case CONSTANT_WAVE_SIGNAL_PIPE_ENUM: {
			pipeRepr = new ConstantWavePipeRepr((ConstantWavePipe) pipe, pipeName);
			return pipeRepr;
		}
		case AUDIO_PLAYER_PIPE_ENUM: {
			pipeRepr = new AudioPlayerPipeRepr((AudioPlayerPipe) pipe, pipeName);
			return pipeRepr;
		}
		case OSCILLOSCOPE_PIPE_ENUM: {
			pipeRepr = new OscilloscopePipeRepr((OscilloscopePipe) pipe, pipeName);
			return pipeRepr;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + pipeType);
		}
	}
	
}
