package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;

public class OscilloscopePipe extends AbstractPipe {

	InputPort inputPort;
	
	public OscilloscopePipe() {
		this.inputPort = new InputPort(this);
		this.inputPorts.add(this.inputPort);
	}
	
	@Override
	protected void doWork() {
	}

}
