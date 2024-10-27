package com.terpomo.wavy.ui.awt.pipes;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.TextField;

import com.terpomo.wavy.signals.ConstantWave;
import com.terpomo.wavy.signals.ConstantWavePipe;

public class ConstantWavePipeRepr extends AbstractSignalPipeRepr<ConstantWave, ConstantWavePipe> {

	private static final long serialVersionUID = 8289652143621889982L;
	public static final String FREQUENCY = "Frequency";
	
	LayoutManager contentLayout;
	protected ConstantWavePipe pipe;
	
	private String frequencyStrValue;
	
	private Label frequencyLabel;
	private TextField frequencyField;

	public ConstantWavePipeRepr(ConstantWavePipe pipe, String name) {
		super(pipe, name);
		this.pipe = (ConstantWavePipe) super.pipe;
		this.contentLayout = new GridLayout(1, 1, 10, 10);
		this.frequencyLabel = new Label(FREQUENCY);
		
		this.contentPanel.setLayout(this.contentLayout);
		this.contentPanel.add(this.frequencyLabel);
		
		this.frequencyStrValue = String.valueOf(this.getPipe().getSignal().getFrequency());
		this.frequencyField = new TextField(this.frequencyStrValue);
		this.contentPanel.add(frequencyField);
	}
}
