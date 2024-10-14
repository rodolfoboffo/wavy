package wavy.flow;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Port implements IPort {

	protected IPipe pipe;
	protected IPort linkedPort;
	protected Queue<Float> buffer;
	
	public Port(IPipe pipe) {
		super();
		this.pipe = pipe;
		this.buffer = new ArrayBlockingQueue<Float>(DEFAULT_DATASTREAM_BUFER_SIZE);
	}
	
	public IPipe getPipe() {
		return pipe;
	}
	public Queue<Float> getBuffer() {
		return buffer;
	}

	@Override
	public IPort getLinkedPort() {
		return this.linkedPort;
	}
	
	@Override
	public void setLinkedPort(Port p) {
		if (this.linkedPort != null && p != this.linkedPort) {
			this.linkedPort.setLinkedPort(null);
		}
		this.linkedPort = p;
		if (p != null && p.getLinkedPort() != this) {
			p.setLinkedPort(this);
		}
	}
	
}
