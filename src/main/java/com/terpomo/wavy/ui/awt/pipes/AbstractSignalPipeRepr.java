package com.terpomo.wavy.ui.awt.pipes;

import com.terpomo.wavy.signals.AbstractSignalSourcePipe;
import com.terpomo.wavy.signals.Signal;

public class AbstractSignalPipeRepr<S extends Signal, T extends AbstractSignalSourcePipe<S>> extends AbstractPipeRepr {

	private static final long serialVersionUID = 6166077718204009450L;

	public AbstractSignalPipeRepr(T pipe, String name) {
		super(pipe, name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getPipe() {
		return (T) super.getPipe();
	}

}
