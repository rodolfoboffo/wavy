package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.pipes.AbstractSignalSourcePipe;
import com.terpomo.wavy.signals.Signal;

public class AbstractSignalPipeRepr<S extends Signal, T extends AbstractSignalSourcePipe<S>> extends AbstractPipeRepr<T> {

	private static final long serialVersionUID = 6166077718204009450L;
	public static final String OUTPUT_SIGNAL = "Output Signal";
	protected PortRepr outputPortRepr;

	public AbstractSignalPipeRepr(T pipe, String name) {
		super(pipe, name);
		this.outputPortRepr = new PortRepr(this.getPipe().getOutputPort(), this);
	}

	public PortRepr getOutputPortRepr() {
		return outputPortRepr;
	}
	
}
