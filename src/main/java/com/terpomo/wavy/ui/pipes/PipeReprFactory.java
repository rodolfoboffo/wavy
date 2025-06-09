package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.pipes.PipeFactory;
import com.terpomo.wavy.pipes.PipeTypeEnum;

import java.lang.reflect.InvocationTargetException;

public class PipeReprFactory {

	public static AbstractPipeRepr<?> createPipeRepr(PipeTypeEnum pipeType, IPipe pipe) {
        try {
			AbstractPipeRepr<?> pipeRepr;
            pipeRepr = pipeType.getPipeReprClss().getConstructor(new Class[]{pipeType.getPipeClass()}).newInstance(pipe);
			return pipeRepr;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
	}

	public static AbstractPipeRepr<?> createPipeRepr(PipeTypeEnum pipeType, String pipeName) {
		IPipe pipe = PipeFactory.createPipe(pipeType, pipeName);
		return createPipeRepr(pipeType, pipe);
	}
	
}
