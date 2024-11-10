package com.terpomo.wavy.ui.awt.pipes;

import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.sound.player.AudioPlayerPipe;

public class AudioPlayerPipeRepr extends AbstractPipeRepr {

	private static final long serialVersionUID = -7694706800995003061L;
	
	public AudioPlayerPipeRepr(AudioPlayerPipe pipe, String name) {
		super(pipe, name);
		this.createPipePropertiesForInputs(pipe);
		this.layoutPipePropertiesOnGrid();
	}
	
	protected void createPipePropertiesForInputs(AudioPlayerPipe pipe) {
		for (int i = 0; i < pipe.getInputPorts().size(); i++) {
			IPort port = pipe.getInputPorts().get(i);
			String propertyName = String.format("Channel %d", i+1);
			@SuppressWarnings({ "rawtypes", "unchecked" })
			PipePropertyRepr pipeProperty = new PipePropertyRepr(null, this, port, propertyName, null, null);
			this.addPipeProperty(pipeProperty);
		}
	}

}
