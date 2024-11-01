package com.terpomo.wavy.flow;

import com.terpomo.wavy.Constants;

public enum PipeTypeEnum {
	
	CONSTANT_WAVE_SIGNAL_PIPE_ENUM(Constants.CONSTANT_WAVE);
	
	private final String friendlyName;
	
	private PipeTypeEnum(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}
	
}
