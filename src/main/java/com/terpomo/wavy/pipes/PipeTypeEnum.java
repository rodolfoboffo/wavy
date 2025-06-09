package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.pipes.filters.BandPassFilterPipe;
import com.terpomo.wavy.pipes.input.FileReaderPipe;
import com.terpomo.wavy.pipes.input.MicPipe;
import com.terpomo.wavy.pipes.input.RTLPipe;
import com.terpomo.wavy.pipes.misc.SplitterPipe;
import com.terpomo.wavy.pipes.modulation.FMDemodulationPipe;
import com.terpomo.wavy.pipes.modulation.FMModulationPipe;
import com.terpomo.wavy.pipes.modulation.IQDemodulationPipe;
import com.terpomo.wavy.pipes.modulation.IQModulationPipe;
import com.terpomo.wavy.pipes.monitors.FFTPipe;
import com.terpomo.wavy.pipes.monitors.OscilloscopePipe;
import com.terpomo.wavy.pipes.operators.CombinationPipe;
import com.terpomo.wavy.pipes.operators.MultiplicationPipe;
import com.terpomo.wavy.pipes.output.AudioPlayerPipe;
import com.terpomo.wavy.pipes.output.FileWriterPipe;
import com.terpomo.wavy.pipes.sources.ConstantValuePipe;
import com.terpomo.wavy.pipes.sources.ConstantWavePipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.filters.BandPassFilterPipeRepr;
import com.terpomo.wavy.ui.pipes.input.MicPipeRepr;
import com.terpomo.wavy.ui.pipes.input.RTLPipeRepr;
import com.terpomo.wavy.ui.pipes.misc.SplitterPipeRepr;
import com.terpomo.wavy.ui.pipes.modulation.FMDemodulationPipeRepr;
import com.terpomo.wavy.ui.pipes.modulation.FMModulationPipeRepr;
import com.terpomo.wavy.ui.pipes.modulation.IQDemodulationPipeRepr;
import com.terpomo.wavy.ui.pipes.modulation.IQModulationPipeRepr;
import com.terpomo.wavy.ui.pipes.monitors.FFTPipeRepr;
import com.terpomo.wavy.ui.pipes.monitors.OscilloscopePipeRepr;
import com.terpomo.wavy.ui.pipes.operators.CombinationPipeRepr;
import com.terpomo.wavy.ui.pipes.operators.MultiplicationPipeRepr;
import com.terpomo.wavy.ui.pipes.output.AudioPlayerPipeRepr;
import com.terpomo.wavy.ui.pipes.output.FileWriterPipeRepr;
import com.terpomo.wavy.ui.pipes.sources.ConstantValuePipeRepr;
import com.terpomo.wavy.ui.pipes.sources.ConstantWavePipeRepr;

public enum PipeTypeEnum {
	
	CONSTANT_WAVE_SIGNAL_PIPE_ENUM(Constants.CONSTANT_WAVE, ConstantWavePipe.class, ConstantWavePipeRepr.class),
	CONSTANT_VALUE_SIGNAL_PIPE_ENUM(Constants.CONSTANT_VALUE, ConstantValuePipe.class, ConstantValuePipeRepr.class),
	AUDIO_PLAYER_PIPE_ENUM(Constants.AUDIO_PLAYER, AudioPlayerPipe.class, AudioPlayerPipeRepr.class),
	OSCILLOSCOPE_PIPE_ENUM(Constants.OSCILLOSCOPE, OscilloscopePipe.class, OscilloscopePipeRepr.class),
	SPLITTER_PIPE_ENUM(Constants.SPLITTER, SplitterPipe.class, SplitterPipeRepr.class),
	FILE_READER_PIPE_ENUM(Constants.FILE_READER, FileReaderPipe.class, FileWriterPipeRepr.class),
	FILE_WRITER_PIPE_ENUM(Constants.FILE_WRITER, FileWriterPipe.class, FileWriterPipeRepr.class),
	FFT_PIPE_ENUM(Constants.FFT, FFTPipe.class, FFTPipeRepr.class),
	COMBINATION_PIPE_ENUM(Constants.COMBINATION, CombinationPipe.class, CombinationPipeRepr.class),
	BAND_PASS_FILTER_PIPE_ENUM(Constants.BAND_PASS_FILTER, BandPassFilterPipe.class, BandPassFilterPipeRepr.class),
	MIC_PIPE_ENUM(Constants.MICROPHONE, MicPipe.class, MicPipeRepr.class),
	FM_MODULATION_PIPE_ENUM(Constants.FM_MODULATION, FMModulationPipe.class, FMModulationPipeRepr.class),
	FM_DEMODULATION_PIPE_ENUM(Constants.FM_DEMODULATION, FMDemodulationPipe.class, FMDemodulationPipeRepr.class),
	IQ_MODULATION_PIPE_ENUM(Constants.IQ_MODULATION, IQModulationPipe.class, IQModulationPipeRepr.class),
	IQ_DEMODULATION_PIPE_ENUM(Constants.IQ_DEMODULATION, IQDemodulationPipe.class, IQDemodulationPipeRepr.class),
	RTLSDR_PIPE_ENUM(Constants.RTLSDR, RTLPipe.class, RTLPipeRepr.class),
	MULTIPLICATION_PIPE_ENUM(Constants.MULTIPLICATION, MultiplicationPipe.class, MultiplicationPipeRepr.class);

	private final String friendlyName;
	private final Class<? extends IPipe> pipeClss;
	private final Class<? extends AbstractPipeRepr<?>> pipeReprClss;

    private PipeTypeEnum(String friendlyName, Class<? extends IPipe> pipeClss, Class<? extends AbstractPipeRepr<?>> pipeReprClss) {
		this.friendlyName = friendlyName;
		this.pipeClss = pipeClss;
        this.pipeReprClss = pipeReprClss;
    }
	
	public String getFriendlyName() {
		return friendlyName;
	}

	public Class<? extends IPipe> getPipeClass() {
		return pipeClss;
	}

    public Class<? extends AbstractPipeRepr<?>> getPipeReprClss() {
		return pipeReprClss;
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
