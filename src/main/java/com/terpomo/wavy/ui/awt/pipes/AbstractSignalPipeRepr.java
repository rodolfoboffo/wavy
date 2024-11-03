package com.terpomo.wavy.ui.awt.pipes;

import com.terpomo.wavy.signals.AbstractSignalSourcePipe;
import com.terpomo.wavy.signals.Signal;

public class AbstractSignalPipeRepr<S extends Signal, T extends AbstractSignalSourcePipe<S>> extends AbstractPipeRepr {

	private static final long serialVersionUID = 6166077718204009450L;
	public static final String OUTPUT_SIGNAL = "Output Signal";
	protected PortRepr outputPortRepr;

	public AbstractSignalPipeRepr(T pipe, String name) {
		super(pipe, name);
		this.outputPortRepr = new PortRepr(this.getPipe().getOutputPort(), this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getPipe() {
		return (T) super.getPipe();
	}

	public PortRepr getOutputPortRepr() {
		return outputPortRepr;
	}
	
}
