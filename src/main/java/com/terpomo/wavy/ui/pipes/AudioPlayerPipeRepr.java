package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.pipes.AudioPlayerPipe;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class AudioPlayerPipeRepr extends AbstractPipeRepr<AudioPlayerPipe> {

	private static final long serialVersionUID = -7694706800995003061L;
	private final GridBagLayout contentLayout;

	public AudioPlayerPipeRepr(AudioPlayerPipe pipe, String name) {
		super(pipe, name);
		this.contentLayout = new GridBagLayout();
		this.getContentPanel().setLayout(this.contentLayout);
		this.buildCustomPipeControls();
		this.getPipe().addPropertyChangeListener(AbstractPipe.PROPERTY_PIPE_INPUT_PORTS, new InputPortsPropertyChangeListener());
	}

	@SuppressWarnings("rawtypes")
    private void buildCustomPipeControls() {
		List<PipePropertyRepr> propertyControls = this.createPipePropertiesForInputs((AudioPlayerPipe) this.getPipe());
		this.layoutPipePropertiesOnGrid(propertyControls);
	}

	@SuppressWarnings("rawtypes")
	protected List<PipePropertyRepr> createPipePropertiesForInputs(AudioPlayerPipe pipe) {
		List<PipePropertyRepr> pipeProperties = new ArrayList<PipePropertyRepr>();
		PipePropertyRepr numChannelsProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, "# of Channels", this.getPipe().getNumOfChannels(), null, this.getPipe()::setNumOfChannels);
		pipeProperties.add(numChannelsProperty);
		for (int i = 0; i < pipe.getInputPorts().size(); i++) {
			IPort port = pipe.getInputPorts().get(i);
			String propertyName = String.format("Channel %d", i+1);
			@SuppressWarnings({ "rawtypes", "unchecked" })
			PipePropertyRepr pipeProperty = new PipePropertyRepr(null, this, port, propertyName, null, null);
			pipeProperties.add(pipeProperty);
		}
		return pipeProperties;
	}

	class InputPortsPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					AudioPlayerPipeRepr.this.buildCustomPipeControls();
					AudioPlayerPipeRepr.this.revalidate();
					AudioPlayerPipeRepr.this.repaint();
				}
			});
		}
	}

}
