package com.terpomo.wavy.flow;

import com.terpomo.wavy.core.ObservableObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPipe extends ObservableObject implements IPipe {

	public final static String PROPERTY_PIPE_INPUT_PORTS = "PROPERTY_PIPE_INPUT_PORTS";
	public final static String PROPERTY_PIPE_OUTPUT_PORTS = "PROPERTY_PIPE_OUTPUT_PORTS";
	private final List<InputPort> inputPorts;
	private final List<OutputPort> outputPorts;
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

	synchronized public void setOutputPorts(List<OutputPort> outputPorts) {
		this.outputPorts.clear();
		this.outputPorts.addAll(outputPorts);
	}

	@Override
	public List<InputPort> getInputPorts() {
		return this.inputPorts;
	}

	synchronized public void setInputPorts(List<InputPort> inputPorts) {
		this.inputPorts.clear();
		this.inputPorts.addAll(inputPorts);
	}

	@Override
	synchronized public void initialize() {
		this.isInitialized = true;
	}
	
	@Override
	synchronized public final void process() {
		this.busy = true;
		if (!this.isInitialized()) {
			this.initialize();
		}
		this.doWork();
		this.busy = false;
	};
	
	protected abstract void doWork();

	@Override
	synchronized public void dispose() {
		this.isInitialized = false;
	}
}
