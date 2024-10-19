package com.terpomo.wavy.flow;

import java.util.Queue;

public interface IPort {
	public static final int DEFAULT_DATASTREAM_BUFER_SIZE = 10240;
	public IPipe getPipe();
	public Queue<Float> getBuffer();
	public int getBufferCapacity();
	public IPort getLinkedPort();
	public void setLinkedPort(Port p);
}
