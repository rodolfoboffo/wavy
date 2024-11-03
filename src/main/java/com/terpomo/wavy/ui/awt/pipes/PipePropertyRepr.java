package com.terpomo.wavy.ui.awt.pipes;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextField;

import com.terpomo.wavy.flow.IPort;

public class PipePropertyRepr<T> {

	private IPipeRepr parentPipe;
	private IPort inputPort;
	private PortRepr inputPortRepr;
	private String propertyName;
	private Label propertyLabel;
	private T value;
	private TextField valueField;
	private IPort outputPort;
	private PortRepr outputPortRepr;
	
	public PipePropertyRepr(IPipeRepr parentPipe, IPort inputPort, String propertyName, T value, IPort outputPort) {
		super();
		this.parentPipe = parentPipe;
		this.inputPort = inputPort;
		this.inputPortRepr = inputPort != null ? new PortRepr(this.inputPort, this.parentPipe) : null;
		this.propertyName = propertyName;
		this.propertyLabel = new Label(this.propertyName);
		this.value = value;
		this.valueField = value != null ? new TextField(this.value.toString()) : null;
		this.outputPort = outputPort;
		this.outputPortRepr = outputPort != null ? new PortRepr(this.outputPort, this.parentPipe) : null;
	}
	
	public void layoutOnGrid(Container container, int rowIndex) {
		GridBagConstraints constraints;
		
		assert (container.getLayout() instanceof GridBagLayout);
		
		if (this.inputPortRepr != null) {
			constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = rowIndex;
			container.add(this.inputPortRepr, constraints);
		}
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = rowIndex;
		constraints.anchor = GridBagConstraints.EAST;
		container.add(this.propertyLabel, constraints);
		
		if (this.valueField != null) {
			constraints = new GridBagConstraints();
			constraints.gridx = 2;
			constraints.gridy = rowIndex;
			container.add(this.valueField, constraints);
		}
		
		if (this.outputPortRepr != null) {
			constraints = new GridBagConstraints();
			constraints.gridx = 3;
			constraints.gridy = rowIndex;
			container.add(this.outputPortRepr, constraints);
		}
	}
	
}
