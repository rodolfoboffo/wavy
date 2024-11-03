package com.terpomo.wavy.ui.awt.pipes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextField;

import com.terpomo.wavy.signals.ConstantWave;
import com.terpomo.wavy.signals.ConstantWavePipe;

public class ConstantWavePipeRepr extends AbstractSignalPipeRepr<ConstantWave, ConstantWavePipe> {

	private static final long serialVersionUID = 8289652143621889982L;
	public static final String FREQUENCY = "Frequency";
	
	GridBagLayout contentLayout;
	protected ConstantWavePipe pipe;
	
	private String frequencyStrValue;

	private Label frequencyLabel;
	private TextField frequencyField;
	private Label outSignalLabel;

	public ConstantWavePipeRepr(ConstantWavePipe pipe, String name) {
		super(pipe, name);
		GridBagConstraints constraints;
		this.pipe = (ConstantWavePipe) super.pipe;
		this.contentLayout = new GridBagLayout();
		this.contentPanel.setLayout(this.contentLayout);
		
		this.frequencyLabel = new Label(FREQUENCY);
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		this.contentPanel.add(frequencyLabel, constraints);
		
		this.frequencyStrValue = String.valueOf(this.getPipe().getSignal().getFrequency());
		this.frequencyField = new TextField(this.frequencyStrValue);
		constraints = new GridBagConstraints();
		constraints.gridx = 2;
		constraints.gridy = 0;
		this.contentPanel.add(frequencyField, constraints);
		
		this.outSignalLabel = new Label(OUTPUT_SIGNAL);
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		this.contentPanel.add(this.outSignalLabel, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 3;
		constraints.gridy = 1;
		this.contentPanel.add(this.getOutputPortRepr(), constraints);
	}
}
