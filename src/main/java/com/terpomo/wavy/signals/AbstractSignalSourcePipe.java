package com.terpomo.wavy.signals;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.Port;

public abstract class AbstractSignalSourcePipe extends AbstractPipe {
	
	protected Signal signal;
	protected Port outputPort;
	
	public AbstractSignalSourcePipe(Signal signal) {
		super();
		this.signal = signal;
		this.outputPort = new Port(this);
		this.outputPorts.add(this.outputPort);
	}
	
	public Port getOutputPort() {
		return outputPort;
	}

	@Override
	protected void doWork() {
		if (this.outputPort.getLinkedPort() != null &&
				this.outputPort.getLinkedPort().getBuffer().size() < this.outputPort.getLinkedPort().getBufferCapacity()) {
			float v = this.signal.getNextValue();
			this.outputPort.getLinkedPort().getBuffer().add(v);
		}
	}

}
