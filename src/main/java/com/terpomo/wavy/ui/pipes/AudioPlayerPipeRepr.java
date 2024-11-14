package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.pipes.AudioPlayerPipe;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class AudioPlayerPipeRepr extends AbstractPipeRepr {

	@Serial
	private static final long serialVersionUID = -7694706800995003061L;
	
	public AudioPlayerPipeRepr(AudioPlayerPipe pipe, String name) {
		super(pipe, name);

		@SuppressWarnings("rawtypes")
		List<PipePropertyRepr> pipeProperties = this.createPipePropertiesForInputs(pipe);
		this.layoutPipePropertiesOnGrid(pipeProperties);
	}

	@SuppressWarnings("rawtypes")
	protected List<PipePropertyRepr> createPipePropertiesForInputs(AudioPlayerPipe pipe) {
		List<PipePropertyRepr> pipeProperties = new ArrayList<PipePropertyRepr>();
		for (int i = 0; i < pipe.getInputPorts().size(); i++) {
			IPort port = pipe.getInputPorts().get(i);
			String propertyName = String.format("Channel %d", i+1);
			@SuppressWarnings({ "rawtypes", "unchecked" })
			PipePropertyRepr pipeProperty = new PipePropertyRepr(null, this, port, propertyName, null, null);
			pipeProperties.add(pipeProperty);
		}
		return pipeProperties;
	}

}
