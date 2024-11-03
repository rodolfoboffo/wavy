package com.terpomo.wavy.flow;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPipe implements IPipe {

	protected List<InputPort> inputPorts;
	protected List<OutputPort> outputPorts;
	private boolean busy = false;
	
	public boolean isBusy() {
		return busy;
	}
	
	public AbstractPipe() {
		this.inputPorts = new ArrayList<InputPort>();
		this.outputPorts = new ArrayList<OutputPort>();
	}
	
	@Override
	public List<OutputPort> getOutputPorts() {
		return this.outputPorts;
	}

	@Override
	public List<InputPort> getInputPorts() {
		return this.inputPorts;
	}

	@Override
	public void initialize() {}
	
	@Override
	public synchronized final void process() {
		this.busy = true;
		this.doWork();
		this.busy = false;
	};
	
	protected abstract void doWork();

	@Override
	public void dispose() {}
}
