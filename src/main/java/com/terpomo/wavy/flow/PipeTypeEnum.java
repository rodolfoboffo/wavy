package com.terpomo.wavy.flow;

import com.terpomo.wavy.Constants;

public enum PipeTypeEnum {
	
	CONSTANT_WAVE_SIGNAL_PIPE_ENUM(Constants.CONSTANT_WAVE),
	AUDIO_PLAYER_PIPE_ENUM(Constants.AUDIO_PLAYER),
	OSCILLOSCOPE_PIPE_ENUM(Constants.OSCILLOSCOPE),
	SPLITTER_PIPE_ENUM(Constants.SPLITTER);
	
	private final String friendlyName;
	
	private PipeTypeEnum(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}
	
}
