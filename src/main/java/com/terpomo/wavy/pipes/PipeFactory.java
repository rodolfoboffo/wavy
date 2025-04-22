package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
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
		case FILE_WRITER_PIPE_ENUM: {
			return new FileWriterPipe();
		}
		case FFT_PIPE_ENUM: {
			return new FFTPipe();
		}
		case COMBINATION_PIPE_ENUM: {
			return new CombinationPipe();
		}
		case BAND_PASS_FILTER_PIPE_ENUM: {
			return new BandPassFilterPipe();
		}
		case MIC_PIPE_ENUM: {
			return new MicPipe();
		}
		case FM_MODULATION_PIPE_ENUM: {
			return new FMModulationPipe();
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + pipeType);
		}
	}
	
}
