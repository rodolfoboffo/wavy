package com.terpomo.wavy.flow;

import com.terpomo.wavy.IWavyDisposable;
import com.terpomo.wavy.core.IObservableObject;
import com.terpomo.wavy.core.IWavyModel;

public interface IPort extends IObservableObject, IWavyModel, IWavyDisposable {
	public static final String LINKED_PORT_PROPERTY = "LINKED_PORT_PROPERTY";

	public IPipe getPipe();
	public Buffer getBuffer();
	public IPort getLinkedPort();
	public void setLinkedPort(IPort p);
}
