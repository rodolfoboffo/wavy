package com.terpomo.wavy.flow;

public abstract class Port implements IPort {

	private final IPipe pipe;
	private final Buffer buffer;
	protected IPort linkedPort;
	
	public Port(IPipe pipe) {
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
		if (this.linkedPort != null && p != this.linkedPort) {
			this.linkedPort.setLinkedPort(null);
		}
		this.linkedPort = p;
		if (p != null && p.getLinkedPort() != this) {
			p.setLinkedPort(this);
		}
	}
	
}
