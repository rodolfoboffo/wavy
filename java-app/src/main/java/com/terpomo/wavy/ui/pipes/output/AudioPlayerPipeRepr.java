package com.terpomo.wavy.ui.pipes.output;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.pipes.output.AudioPlayerPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class AudioPlayerPipeRepr extends AbstractPipeRepr<AudioPlayerPipe> {

	private static final long serialVersionUID = -7694706800995003061L;
	private static final String MIXER = "Mixer";
	private static final String VOLUME = "Volume (%)";
	private static final String SAMPLE_RATE = "Sample Rate";
	private static final String NUM_CHANNELS = "# of Channels";
	private static final String CHANNEL_NUMBER = "Channel %d";
	private JPanel mixerPanel;
	private JPanel pipePropertiesPanel;

    public AudioPlayerPipeRepr(AudioPlayerPipe pipe) {
		super(pipe);
		this.createPanels();
		this.createMixerSelectorControls();
		this.buildCustomPipeControls();
		this.getPipe().addPropertyChangeListener(AbstractPipe.PROPERTY_PIPE_INPUT_PORTS, new InputPortsPropertyChangeListener());
	}

	@SuppressWarnings("rawtypes")
    private void buildCustomPipeControls() {
		List<PipePropertyRepr<?>> propertyControls = this.createPipePropertiesForInputs();
		this.layoutPipePropertiesOnGrid(this.pipePropertiesPanel, propertyControls);
	}

	private void createMixerSelectorControls() {
		GridBagConstraints constraints;

        JLabel labelMixer = new JLabel(MIXER);
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		this.mixerPanel.add(labelMixer, constraints);

        JComboBox<String> comboMixerInfos = new JComboBox<>(this.getPipe().getMixerInfos());
		comboMixerInfos.addActionListener(e -> {
            @SuppressWarnings("unchecked") JComboBox<String> combo = (JComboBox<String>) e.getSource();
            AudioPlayerPipeRepr.this.getPipe().setMixerName(combo.getSelectedItem().toString());
        });
		if (this.getPipe().getMixerName() != null)
			comboMixerInfos.setSelectedItem(this.getPipe().getMixerName());
		else if (comboMixerInfos.getItemCount() > 0)
			comboMixerInfos.setSelectedIndex(0);
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		this.mixerPanel.add(comboMixerInfos, constraints);
	}

	private void createPanels() {
		GridBagLayout contentLayout = new GridBagLayout();
		JPanel contentPanel = getContentPanel();
		contentPanel.setLayout(contentLayout);

		GridBagLayout inputFileLayout = new GridBagLayout();
		this.mixerPanel = new JPanel();
		this.mixerPanel.setLayout(inputFileLayout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		contentPanel.add(this.mixerPanel, constraints);

		GridBagLayout pipePropertiesLayout = new GridBagLayout();
		this.pipePropertiesPanel = new JPanel();
		this.pipePropertiesPanel.setLayout(pipePropertiesLayout);
		constraints.gridx = 0;
		constraints.gridy = 1;
		contentPanel.add(this.pipePropertiesPanel, constraints);
	}

	@SuppressWarnings("rawtypes")
	protected List<PipePropertyRepr<?>> createPipePropertiesForInputs() {
		List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();
		PipePropertyRepr sampleRateProperty = new PipePropertyRepr<>(Integer.class, this, null, SAMPLE_RATE, this.getPipe()::getSampleRate, null, this.getPipe()::setSampleRate);
		pipeProperties.add(sampleRateProperty);
		PipePropertyRepr numChannelsProperty = new PipePropertyRepr<>(Integer.class, this, null, NUM_CHANNELS, this.getPipe()::getNumOfChannels, null, this.getPipe()::setNumOfChannels);
		pipeProperties.add(numChannelsProperty);
		PipePropertyRepr volumesProperty = new PipePropertyRepr<>(Float.class, this, null, VOLUME, this.getPipe()::getVolume, null, this.getPipe()::setVolume);
		pipeProperties.add(volumesProperty);
		for (int i = 0; i < this.getPipe().getInputPorts().size(); i++) {
			IPort port = this.getPipe().getInputPorts().get(i);
			String propertyName = String.format(CHANNEL_NUMBER, i+1);
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
