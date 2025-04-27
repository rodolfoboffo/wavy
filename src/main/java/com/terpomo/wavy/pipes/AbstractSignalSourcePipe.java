package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.SignalBuffer;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.flow.AbstractPort;
import com.terpomo.wavy.signals.Signal;

public abstract class AbstractSignalSourcePipe<T extends Signal> extends AbstractPipe {
	
	protected T signal;
	protected OutputPort outputPort;
	
	public AbstractSignalSourcePipe(T signal) {
		super();
		this.signal = signal;
		this.outputPort = new OutputPort(this);
		this.getOutputPorts().add(this.outputPort);
	}
	
	public AbstractPort getOutputPort() {
		return outputPort;
	}

	@Override
	protected void doWork() {
		if (this.outputPort.getLinkedPort() != null) {
			SignalBuffer buffer = this.outputPort.getLinkedPort().getBuffer();
			if (!buffer.isFull()) {
				float v = this.signal.getNextValue();
				this.putThroughPort(outputPort, v);
			}
		}
	}
	
	public T getSignal() {
		return signal;
	}

}
