package com.terpomo.wavy.flow;

public interface IPort {
	public IPipe getPipe();
	public Buffer getBuffer();
	public IPort getLinkedPort();
	public void setLinkedPort(IPort p);
}
