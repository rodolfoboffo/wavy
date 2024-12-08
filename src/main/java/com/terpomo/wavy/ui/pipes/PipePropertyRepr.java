package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.IPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.function.Consumer;

public class PipePropertyRepr<T> {

	private static final int FIELD_PADDING = 4;
	private static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 12);
	private final Class<T> clazz;
	private final AbstractPipeRepr<?> parentPipe;
	private final IPort inputPort;
	private final PortRepr inputPortRepr;
	private final String propertyName;
	private final JLabel propertyLabel;
	private T value;
	private final JTextField valueField;
	private final IPort outputPort;
	private final PortRepr outputPortRepr;
	private final Consumer<T> callback;
	private final boolean readOnly;
	
	public PipePropertyRepr(Class<T> clazz, AbstractPipeRepr<?> parentPipe, IPort inputPort, String propertyName, T value, IPort outputPort, Consumer<T> callback, boolean readOnly) {
		super();
		this.clazz = clazz;
		this.callback = callback;
		this.readOnly = readOnly;
		this.parentPipe = parentPipe;
		this.inputPort = inputPort;
		this.inputPortRepr = inputPort != null ? new PortRepr(this.inputPort, this.parentPipe) : null;
		this.propertyName = propertyName;
		this.propertyLabel = new JLabel(this.propertyName);
		this.propertyLabel.setFont(DEFAULT_FONT);
		this.value = value;
		if (value != null) {
			String textValue = this.getTextValue(value);
			this.valueField = new JTextField(textValue);
			this.valueField.addFocusListener(new ValueFieldFocusListener());
			this.valueField.setEnabled(!readOnly);
		}
		else {
			this.valueField = null;
		}
		this.outputPort = outputPort;
		this.outputPortRepr = outputPort != null ? new PortRepr(this.outputPort, this.parentPipe) : null;
	}
	
	public PipePropertyRepr(Class<T> clazz, AbstractPipeRepr<?> parentPipe, IPort inputPort, String propertyName, T value, IPort outputPort) {
		this(clazz, parentPipe, inputPort, propertyName, value, outputPort, null, false);
	}

	public PipePropertyRepr(Class<T> clazz, AbstractPipeRepr<?> parentPipe, IPort inputPort, String propertyName, T value, IPort outputPort, boolean readOnly) {
		this(clazz, parentPipe, inputPort, propertyName, value, outputPort, null, readOnly);
	}

	public PipePropertyRepr(Class<T> clazz, AbstractPipeRepr<?> parentPipe, IPort inputPort, String propertyName, T value, IPort outputPort, Consumer<T> callback) {
		this(clazz, parentPipe, inputPort, propertyName, value, outputPort, callback, false);
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
	protected T parseTextValue(String textValue) throws ParseException {
		Object value = null;
		if (this.clazz.equals(Float.class))
			value = NumberFormat.getInstance().parse(textValue).floatValue();
		if (this.clazz.equals(Integer.class))
			value = NumberFormat.getInstance().parse(textValue).intValue();
		return (T)value;
	}

	public String getTextValue(T value) {
		String text = "";
		if (this.clazz.equals(Float.class))
			text = NumberFormat.getInstance().format(value);
		if (this.clazz.equals(Integer.class))
			text = NumberFormat.getInstance().format(value);
		return text;
	}
	
	class ValueFieldFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
		}

		@Override
		public void focusLost(FocusEvent e) {
			JTextField field = (JTextField) e.getComponent();
			String valueText = field.getText();
            T value = null;
            try {
                value = PipePropertyRepr.this.parseTextValue(valueText);
				PipePropertyRepr.this.callback.accept(value);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
		}
		
	}
	
}
