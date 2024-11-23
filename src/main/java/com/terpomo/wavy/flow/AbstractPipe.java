package com.terpomo.wavy.flow;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPipe implements IPipe {

	private List<InputPort> inputPorts;
	private List<OutputPort> outputPorts;
	private boolean isInitialized;
	private boolean busy = false;
	
	public boolean isBusy() {
		return busy;
	}
	
	public AbstractPipe() {
		this.inputPorts = new ArrayList<InputPort>();
		this.outputPorts = new ArrayList<OutputPort>();
		this.isInitialized = false;
	}

	@Override
	public boolean isInitialized() {
		return isInitialized;
	}

	@Override
	public List<OutputPort> getOutputPorts() {
		return this.outputPorts;
	}

	public void setOutputPorts(List<OutputPort> outputPorts) {
		this.outputPorts.clear();
		this.outputPorts.addAll(outputPorts);
	}

	@Override
	public List<InputPort> getInputPorts() {
		return this.inputPorts;
	}

	public void setInputPorts(List<InputPort> inputPorts) {
		this.inputPorts.clear();
		this.inputPorts.addAll(inputPorts);
	}

	@Override
	public void initialize() {
		this.isInitialized = true;
	}
	
	@Override
	public synchronized final void process() {
		this.busy = true;
		if (!this.isInitialized()) {
			this.initialize();
		}
		this.doWork();
		this.busy = false;
	};
	
	protected abstract void doWork();

	@Override
	public void dispose() {}
}
