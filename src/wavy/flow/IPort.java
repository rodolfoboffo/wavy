package wavy.flow;

import java.util.Queue;

public interface IPort {
	public static final int DEFAULT_DATASTREAM_BUFER_SIZE = 1024;
	public IPipe getPipe();
	public Queue<Float> getBuffer();
	public IPort getLinkedPort();
	public void setLinkedPort(Port p);
}
