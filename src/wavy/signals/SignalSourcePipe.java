package wavy.signals;

import wavy.flow.AbstractPipe;
import wavy.flow.Port;

public class SignalSourcePipe extends AbstractPipe {
	
	protected Signal signal;
	protected Port outputPort;
	
	public SignalSourcePipe(Signal signal) {
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
