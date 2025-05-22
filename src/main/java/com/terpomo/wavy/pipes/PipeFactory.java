package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
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
import com.terpomo.wavy.signals.ConstantWave;

public class PipeFactory {
	
	public static AbstractPipe createPipe(PipeTypeEnum pipeType, String pipeName) {
		switch (pipeType) {
		case CONSTANT_WAVE_SIGNAL_PIPE_ENUM: {
			return new ConstantWavePipe(new ConstantWave(), pipeName);
		}
		case AUDIO_PLAYER_PIPE_ENUM: {
			return new AudioPlayerPipe(pipeName);
		}
		case OSCILLOSCOPE_PIPE_ENUM: {
			return new OscilloscopePipe(pipeName);
		}
		case SPLITTER_PIPE_ENUM: {
			return new SplitterPipe(pipeName);
		}
		case FILE_READER_PIPE_ENUM: {
			return new FileReaderPipe(pipeName);
		}
		case FILE_WRITER_PIPE_ENUM: {
			return new FileWriterPipe(pipeName);
		}
		case FFT_PIPE_ENUM: {
			return new FFTPipe(pipeName);
		}
		case COMBINATION_PIPE_ENUM: {
			return new CombinationPipe(pipeName);
		}
		case BAND_PASS_FILTER_PIPE_ENUM: {
			return new BandPassFilterPipe(pipeName);
		}
		case MIC_PIPE_ENUM: {
			return new MicPipe(pipeName);
		}
		case FM_MODULATION_PIPE_ENUM: {
			return new FMModulationPipe(pipeName);
		}
		case FM_DEMODULATION_PIPE_ENUM: {
			return new FMDemodulationPipe(pipeName);
		}
		case IQ_MODULATION_PIPE_ENUM: {
			return new IQModulationPipe(pipeName);
		}
		case IQ_DEMODULATION_PIPE_ENUM: {
			return new IQDemodulationPipe(pipeName);
		}
		case RTLSDR_PIPE_ENUM: {
			return new RTLPipe(pipeName);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + pipeType);
		}
	}
	
}
