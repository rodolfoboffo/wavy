package com.terpomo.wavy.flow;

import com.terpomo.wavy.core.ObservableObject;

public abstract class AbstractPort extends ObservableObject implements IPort {

	private final IPipe pipe;
	private final Buffer buffer;
	protected IPort linkedPort;
	
	public AbstractPort(IPipe pipe) {
		super();
		this.pipe = pipe;
		this.buffer = new Buffer();
	}
	
	public IPipe getPipe() {
		return pipe;
	}
	public Buffer getBuffer() {
		return buffer;
	}

	@Override
	public IPort getLinkedPort() {
		return this.linkedPort;
	}
	
	@Override
	public void setLinkedPort(IPort p) {
		IPort previousLinkedPort = this.linkedPort;
		this.linkedPort = p;
		if (previousLinkedPort != null && p != previousLinkedPort) {
			previousLinkedPort.setLinkedPort(null);
		}
		if (p != null && p.getLinkedPort() != this) {
			p.setLinkedPort(this);
		}
		this.firePropertyChange(LINKED_PORT_PROPERTY, previousLinkedPort, p);
	}
	
}
