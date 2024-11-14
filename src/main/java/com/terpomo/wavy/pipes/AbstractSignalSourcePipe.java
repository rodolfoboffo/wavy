package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.flow.Port;
import com.terpomo.wavy.signals.Signal;

public abstract class AbstractSignalSourcePipe<T extends Signal> extends AbstractPipe {
	
	protected T signal;
	protected OutputPort outputPort;
	
	public AbstractSignalSourcePipe(T signal) {
		super();
		this.signal = signal;
		this.outputPort = new OutputPort(this);
		this.outputPorts.add(this.outputPort);
	}
	
	public Port getOutputPort() {
		return outputPort;
	}

	@Override
	protected void doWork() {
		if (this.outputPort.getLinkedPort() != null &&
				this.outputPort.getLinkedPort().getBuffer().getSize() < this.outputPort.getLinkedPort().getBuffer().getCapacity()) {
			float v = this.signal.getNextValue();
			this.outputPort.getLinkedPort().getBuffer().put(v);
		}
	}
	
	public T getSignal() {
		return signal;
	}

}
