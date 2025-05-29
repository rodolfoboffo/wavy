package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.IPipe;
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

public enum PipeTypeEnum {
	
	CONSTANT_WAVE_SIGNAL_PIPE_ENUM(Constants.CONSTANT_WAVE, ConstantWavePipe.class),
	AUDIO_PLAYER_PIPE_ENUM(Constants.AUDIO_PLAYER, AudioPlayerPipe.class),
	OSCILLOSCOPE_PIPE_ENUM(Constants.OSCILLOSCOPE, OscilloscopePipe.class),
	SPLITTER_PIPE_ENUM(Constants.SPLITTER, SplitterPipe.class),
	FILE_READER_PIPE_ENUM(Constants.FILE_READER, FileReaderPipe.class),
	FILE_WRITER_PIPE_ENUM(Constants.FILE_WRITER, FileWriterPipe.class),
	FFT_PIPE_ENUM(Constants.FFT, FFTPipe.class),
	COMBINATION_PIPE_ENUM(Constants.COMBINATION, CombinationPipe.class),
	BAND_PASS_FILTER_PIPE_ENUM(Constants.BAND_PASS_FILTER, BandPassFilterPipe.class),
	MIC_PIPE_ENUM(Constants.MICROPHONE, MicPipe.class),
	FM_MODULATION_PIPE_ENUM(Constants.FM_MODULATION, FMModulationPipe.class),
	FM_DEMODULATION_PIPE_ENUM(Constants.FM_DEMODULATION, FMDemodulationPipe.class),
	IQ_MODULATION_PIPE_ENUM(Constants.IQ_MODULATION, IQModulationPipe.class),
	IQ_DEMODULATION_PIPE_ENUM(Constants.IQ_DEMODULATION, IQDemodulationPipe.class),
	RTLSDR_PIPE_ENUM(Constants.RTLSDR, RTLPipe.class);

	private final String friendlyName;
	private final Class<? extends IPipe> pipeClss;
	
	private PipeTypeEnum(String friendlyName, Class<? extends IPipe> pipeClss) {
		this.friendlyName = friendlyName;
		this.pipeClss = pipeClss;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}

	public Class<? extends IPipe> getPipeClass() {
		return pipeClss;
	}

	public static PipeTypeEnum valueOf(Class<? extends IPipe> clss) {
		for (PipeTypeEnum pipeType : PipeTypeEnum.values()) {
			if (pipeType.getPipeClass().equals(clss)) {
				return pipeType;
			}
		}
		throw new RuntimeException(String.format("Could not get pipe type enum from class %s", clss.getName()));
	}
}
