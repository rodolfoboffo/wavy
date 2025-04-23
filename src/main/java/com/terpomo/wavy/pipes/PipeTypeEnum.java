package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;

public enum PipeTypeEnum {
	
	CONSTANT_WAVE_SIGNAL_PIPE_ENUM(Constants.CONSTANT_WAVE),
	AUDIO_PLAYER_PIPE_ENUM(Constants.AUDIO_PLAYER),
	OSCILLOSCOPE_PIPE_ENUM(Constants.OSCILLOSCOPE),
	SPLITTER_PIPE_ENUM(Constants.SPLITTER),
	FILE_READER_PIPE_ENUM(Constants.FILE_READER),
	FILE_WRITER_PIPE_ENUM(Constants.FILE_WRITER),
	FFT_PIPE_ENUM(Constants.FFT),
	COMBINATION_PIPE_ENUM(Constants.COMBINATION),
	BAND_PASS_FILTER_PIPE_ENUM(Constants.BAND_PASS_FILTER),
	MIC_PIPE_ENUM(Constants.MICROPHONE),
	FM_MODULATION_PIPE_ENUM(Constants.FM_MODULATION),
	IQ_MODULATION_PIPE_ENUM(Constants.IQ_MODULATION);

	private final String friendlyName;
	
	private PipeTypeEnum(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}
	
}
