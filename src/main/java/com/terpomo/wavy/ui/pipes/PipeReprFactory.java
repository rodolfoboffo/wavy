package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.pipes.PipeFactory;
import com.terpomo.wavy.pipes.PipeTypeEnum;
import com.terpomo.wavy.pipes.*;

public class PipeReprFactory {

	public static AbstractPipeRepr<?> createPipeRepr(PipeTypeEnum pipeType, String pipeName) {
		AbstractPipe pipe = PipeFactory.createPipe(pipeType);
		AbstractPipeRepr<?> pipeRepr;
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
		case SPLITTER_PIPE_ENUM: {
			pipeRepr = new SplitterPipeRepr((SplitterPipe) pipe, pipeName);
			return pipeRepr;
		}
		case FILE_READER_PIPE_ENUM: {
			pipeRepr = new FileReaderPipeRepr((FileReaderPipe) pipe, pipeName);
			return pipeRepr;
		}
		case FILE_WRITER_PIPE_ENUM: {
			pipeRepr = new FileWriterPipeRepr((FileWriterPipe) pipe, pipeName);
			return pipeRepr;
		}
		case FFT_PIPE_ENUM: {
			pipeRepr = new FFTPipeRepr((FFTPipe) pipe, pipeName);
			return pipeRepr;
		}
		case COMBINATION_PIPE_ENUM: {
			pipeRepr = new CombinationPipeRepr((CombinationPipe) pipe, pipeName);
			return pipeRepr;
		}
		case BAND_PASS_FILTER_PIPE_ENUM: {
			pipeRepr = new BandPassFilterPipeRepr((BandPassFilterPipe) pipe, pipeName);
			return pipeRepr;
		}
		case MIC_PIPE_ENUM: {
			pipeRepr = new MicPipeRepr((MicPipe) pipe, pipeName);
			return pipeRepr;
		}
		case FM_MODULATION_PIPE_ENUM: {
			pipeRepr = new FMModulationPipeRepr((FMModulationPipe) pipe, pipeName);
			return pipeRepr;
		}
		case IQ_MODULATION_PIPE_ENUM: {
			pipeRepr = new IQModulationPipeRepr((IQModulationPipe) pipe, pipeName);
			return pipeRepr;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + pipeType);
		}
	}
	
}
