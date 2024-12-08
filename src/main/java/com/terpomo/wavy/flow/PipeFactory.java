package com.terpomo.wavy.flow;

import com.terpomo.wavy.pipes.*;
import com.terpomo.wavy.signals.ConstantWave;

public class PipeFactory {
	
	public static AbstractPipe createPipe(PipeTypeEnum pipeType) {
		switch (pipeType) {
		case CONSTANT_WAVE_SIGNAL_PIPE_ENUM: {
			return new ConstantWavePipe(new ConstantWave());
		}
		case AUDIO_PLAYER_PIPE_ENUM: {
			return new AudioPlayerPipe();
		}
		case OSCILLOSCOPE_PIPE_ENUM: {
			return new OscilloscopePipe();
		}
		case SPLITTER_PIPE_ENUM: {
			return new SplitterPipe();
		}
			case FILE_READER_PIPE_ENUM: {
			return new FileReaderPipe();
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + pipeType);
		}
	}
	
}
