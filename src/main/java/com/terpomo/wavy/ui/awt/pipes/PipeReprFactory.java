package com.terpomo.wavy.ui.awt.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.PipeFactory;
import com.terpomo.wavy.flow.PipeTypeEnum;
import com.terpomo.wavy.signals.ConstantWavePipe;
import com.terpomo.wavy.sound.player.AudioPlayerPipe;

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
		default:
			throw new IllegalArgumentException("Unexpected value: " + pipeType);
		}
	}
	
}
