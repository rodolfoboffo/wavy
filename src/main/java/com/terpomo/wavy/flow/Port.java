package com.terpomo.wavy.flow;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class Port implements IPort {

	protected IPipe pipe;
	protected IPort linkedPort;
	protected Queue<Float> buffer;
	protected final int bufferCapacity;
	
	public Port(IPipe pipe) {
		super();
		this.pipe = pipe;
		this.bufferCapacity = DEFAULT_DATASTREAM_BUFER_SIZE;
		this.buffer = new ArrayBlockingQueue<Float>(this.bufferCapacity);
	}
	
	public IPipe getPipe() {
		return pipe;
	}
	public Queue<Float> getBuffer() {
		return buffer;
	}
	
	public int getBufferCapacity() {
		return bufferCapacity;
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
