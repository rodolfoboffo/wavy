package com.terpomo.wavy.flow;

public class DataStream extends AbstractPipe {

	protected Port inputPort;
	protected Port outputPort;
	
	public DataStream() {
		this.inputPort = new Port(this);
		this.inputPorts.add(inputPort);
		this.outputPort = new Port(this);
		this.outputPorts.add(outputPort);
	}
	
	@Override
	protected void doWork() {
		
	}

}
