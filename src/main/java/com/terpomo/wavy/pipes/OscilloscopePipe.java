package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.Buffer;
import com.terpomo.wavy.flow.InputPort;

import java.util.List;

public class OscilloscopePipe extends AbstractPipe {

	private static final int DEFAULT_OSCILLOSCOPE_BUFFER_SIZE = 102400;
	private final InputPort inputPort;
	private final Buffer buffer;
	
	public OscilloscopePipe() {
		this.inputPort = new InputPort(this);
		this.inputPorts.add(this.inputPort);
		this.buffer = new Buffer(DEFAULT_OSCILLOSCOPE_BUFFER_SIZE);
	}
	
	@Override
	protected void doWork() {
		if (this.buffer.getRemainingCapacity() > 0) {
			List<Float> values = this.inputPort.getBuffer().fetch(this.buffer.getRemainingCapacity());
			this.buffer.putAll(values);
		}
	}

	public Buffer getBuffer() {
		return buffer;
	}
}
