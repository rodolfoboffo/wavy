package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.IPipe;

import java.lang.reflect.InvocationTargetException;

public class PipeFactory {

	public static IPipe createPipe(PipeTypeEnum pipeType, String pipeName) {
		IPipe pipe = PipeFactory.createPipe(pipeType);
		pipe.setName(pipeName);
		return pipe;
	}

	public static IPipe createPipe(PipeTypeEnum pipeType) {
        try {
			IPipe pipe = pipeType.getPipeClass().getConstructor().newInstance();
			return pipe;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
	}
	
}
