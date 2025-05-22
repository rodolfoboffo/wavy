package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.pipes.PipeFactory;
import com.terpomo.wavy.pipes.PipeTypeEnum;
import com.terpomo.wavy.pipes.filters.BandPassFilterPipe;
import com.terpomo.wavy.pipes.input.FileReaderPipe;
import com.terpomo.wavy.pipes.input.MicPipe;
import com.terpomo.wavy.pipes.input.RTLPipe;
import com.terpomo.wavy.pipes.misc.CombinationPipe;
import com.terpomo.wavy.pipes.misc.SplitterPipe;
import com.terpomo.wavy.pipes.modulation.FMDemodulationPipe;
import com.terpomo.wavy.pipes.modulation.FMModulationPipe;
import com.terpomo.wavy.pipes.modulation.IQDemodulationPipe;
import com.terpomo.wavy.pipes.modulation.IQModulationPipe;
import com.terpomo.wavy.pipes.monitors.FFTPipe;
import com.terpomo.wavy.pipes.monitors.OscilloscopePipe;
import com.terpomo.wavy.pipes.output.AudioPlayerPipe;
import com.terpomo.wavy.pipes.output.FileWriterPipe;
import com.terpomo.wavy.pipes.sources.ConstantWavePipe;
import com.terpomo.wavy.ui.pipes.filters.BandPassFilterPipeRepr;
import com.terpomo.wavy.ui.pipes.input.FileReaderPipeRepr;
import com.terpomo.wavy.ui.pipes.input.MicPipeRepr;
import com.terpomo.wavy.ui.pipes.input.RTLPipeRepr;
import com.terpomo.wavy.ui.pipes.misc.CombinationPipeRepr;
import com.terpomo.wavy.ui.pipes.misc.SplitterPipeRepr;
import com.terpomo.wavy.ui.pipes.modulation.*;
import com.terpomo.wavy.ui.pipes.monitors.FFTPipeRepr;
import com.terpomo.wavy.ui.pipes.monitors.OscilloscopePipeRepr;
import com.terpomo.wavy.ui.pipes.output.FileWriterPipeRepr;
import com.terpomo.wavy.ui.pipes.sources.ConstantWavePipeRepr;

public class PipeReprFactory {

	public static AbstractPipeRepr<?> createPipeRepr(PipeTypeEnum pipeType, String pipeName) {
		AbstractPipe pipe = PipeFactory.createPipe(pipeType, pipeName);
		AbstractPipeRepr<?> pipeRepr;
		switch (pipeType) {
		case CONSTANT_WAVE_SIGNAL_PIPE_ENUM: {
			pipeRepr = new ConstantWavePipeRepr((ConstantWavePipe) pipe);
			return pipeRepr;
		}
		case AUDIO_PLAYER_PIPE_ENUM: {
			pipeRepr = new AudioPlayerPipeRepr((AudioPlayerPipe) pipe);
			return pipeRepr;
		}
		case OSCILLOSCOPE_PIPE_ENUM: {
			pipeRepr = new OscilloscopePipeRepr((OscilloscopePipe) pipe);
			return pipeRepr;
		}
		case SPLITTER_PIPE_ENUM: {
			pipeRepr = new SplitterPipeRepr((SplitterPipe) pipe);
			return pipeRepr;
		}
		case FILE_READER_PIPE_ENUM: {
			pipeRepr = new FileReaderPipeRepr((FileReaderPipe) pipe);
			return pipeRepr;
		}
		case FILE_WRITER_PIPE_ENUM: {
			pipeRepr = new FileWriterPipeRepr((FileWriterPipe) pipe);
			return pipeRepr;
		}
		case FFT_PIPE_ENUM: {
			pipeRepr = new FFTPipeRepr((FFTPipe) pipe);
			return pipeRepr;
		}
		case COMBINATION_PIPE_ENUM: {
			pipeRepr = new CombinationPipeRepr((CombinationPipe) pipe);
			return pipeRepr;
		}
		case BAND_PASS_FILTER_PIPE_ENUM: {
			pipeRepr = new BandPassFilterPipeRepr((BandPassFilterPipe) pipe);
			return pipeRepr;
		}
		case MIC_PIPE_ENUM: {
			pipeRepr = new MicPipeRepr((MicPipe) pipe);
			return pipeRepr;
		}
		case FM_MODULATION_PIPE_ENUM: {
			pipeRepr = new FMModulationPipeRepr((FMModulationPipe) pipe);
			return pipeRepr;
		}
		case FM_DEMODULATION_PIPE_ENUM: {
			pipeRepr = new FMDemodulationPipeRepr((FMDemodulationPipe) pipe);
			return pipeRepr;
		}
		case IQ_MODULATION_PIPE_ENUM: {
			pipeRepr = new IQModulationPipeRepr((IQModulationPipe) pipe);
			return pipeRepr;
		}
		case IQ_DEMODULATION_PIPE_ENUM: {
			pipeRepr = new IQDemodulationPipeRepr((IQDemodulationPipe) pipe);
			return pipeRepr;
		}
		case RTLSDR_PIPE_ENUM: {
			pipeRepr = new RTLPipeRepr((RTLPipe) pipe);
			return pipeRepr;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + pipeType);
		}
	}
	
}
