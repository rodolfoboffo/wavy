package com.terpomo.wavy.flow;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPipe implements IPipe {

	protected List<IPort> inputPorts;
	protected List<IPort> outputPorts;
	private boolean busy = false;
	
	public boolean isBusy() {
		return busy;
	}
	
	public AbstractPipe() {
		this.inputPorts = new ArrayList<IPort>();
		this.outputPorts = new ArrayList<IPort>();
	}
	
	@Override
	public List<IPort> getOutputPorts() {
		return this.outputPorts;
	}

	@Override
	public List<IPort> getInputPorts() {
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
