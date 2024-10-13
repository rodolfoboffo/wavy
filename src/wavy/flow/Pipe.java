package wavy.flow;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import wavy.Constants;

public abstract class Pipe {
	protected Queue<Float> buffer;
	
	public Pipe(int bufferSize) {
		this.buffer = new ArrayBlockingQueue<Float>(bufferSize);
	}
	
	public Pipe() {
		this(Constants.DEFAULT_DATASTREAM_BUFER_SIZE);
	}

}
