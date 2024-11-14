package com.terpomo.wavy.ui.pipes;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.terpomo.wavy.flow.IPort;

public class PipePropertyRepr<T> {

	private static final int FIELD_PADDING = 4;
	private static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 12);
	private Class<T> clazz;
	private AbstractPipeRepr parentPipe;
	private IPort inputPort;
	private PortRepr inputPortRepr;
	private String propertyName;
	private JLabel propertyLabel;
	private T value;
	private JTextField valueField;
	private IPort outputPort;
	private PortRepr outputPortRepr;
	private Consumer<T> callback;
	
	public PipePropertyRepr(Class<T> clazz, AbstractPipeRepr parentPipe, IPort inputPort, String propertyName, T value, IPort outputPort, Consumer<T> callback) {
		super();
		this.clazz = clazz;
		this.callback = callback;
		this.parentPipe = parentPipe;
		this.inputPort = inputPort;
		this.inputPortRepr = inputPort != null ? new PortRepr(this.inputPort, this.parentPipe) : null;
		this.propertyName = propertyName;
		this.propertyLabel = new JLabel(this.propertyName);
		this.propertyLabel.setFont(DEFAULT_FONT);
		this.value = value;
		if (value != null) {
			this.valueField = new JTextField(this.value.toString());
			this.valueField.addFocusListener(new ValueFieldFocusListener());
		}
		this.outputPort = outputPort;
		this.outputPortRepr = outputPort != null ? new PortRepr(this.outputPort, this.parentPipe) : null;
	}
	
	public PipePropertyRepr(Class<T> clzz, AbstractPipeRepr parentPipe, IPort inputPort, String propertyName, T value, IPort outputPort) {
		this(clzz, parentPipe, inputPort, propertyName, value, outputPort, null);
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
		constraints.insets = new Insets(0, FIELD_PADDING, 0, FIELD_PADDING);
		container.add(this.propertyLabel, constraints);
		
		if (this.valueField != null) {
			constraints = new GridBagConstraints();
			constraints.gridx = 2;
			constraints.gridy = rowIndex;
			constraints.insets = new Insets(FIELD_PADDING, FIELD_PADDING, FIELD_PADDING, FIELD_PADDING);
			container.add(this.valueField, constraints);
		}
		
		if (this.outputPortRepr != null) {
			constraints = new GridBagConstraints();
			constraints.gridx = 3;
			constraints.gridy = rowIndex;
			constraints.insets = new Insets(FIELD_PADDING, FIELD_PADDING, FIELD_PADDING, FIELD_PADDING);
			container.add(this.outputPortRepr, constraints);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected T parseTextValue(String textValue) {
		Object value = null;
		if (this.clazz.equals(Float.class))
			value = Float.parseFloat(textValue);
		return (T)value;
	}
	
	class ValueFieldFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
		}

		@Override
		public void focusLost(FocusEvent e) {
			JTextField field = (JTextField) e.getComponent();
			String valueText = field.getText();
			T value = PipePropertyRepr.this.parseTextValue(valueText);
			PipePropertyRepr.this.callback.accept(value);
		}
		
	}
	
}
