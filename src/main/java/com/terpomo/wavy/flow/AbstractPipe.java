package com.terpomo.wavy.flow;

import com.terpomo.wavy.core.ObservableObject;
import com.terpomo.wavy.util.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractPipe extends ObservableObject implements IPipe {

	private static final Logger LOGGER = Logger.getLogger(AbstractPipe.class.getName());
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
	
	synchronized public void buildInputPipes(int numOfPipes) {
		this.dispose();
        try {
			List<InputPort> newInputPorts = null;
            newInputPorts = ListUtils.buildNewList(numOfPipes, InputPort.class, this.getInputPorts(), InputPort.class.getDeclaredConstructor(IPipe.class), new Object[]{this});
			this.setInputPorts(newInputPorts);
			this.firePropertyChange(PROPERTY_PIPE_INPUT_PORTS, null, this.getInputPorts());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
	}

	synchronized public void buildOutputPipes(int numOfPipes) {
		this.dispose();
        try {
			List<OutputPort> newOutputPorts = null;
            newOutputPorts = ListUtils.buildNewList(numOfPipes, OutputPort.class, this.getOutputPorts(), OutputPort.class.getDeclaredConstructor(IPipe.class), new Object[]{this});
			this.setOutputPorts(newOutputPorts);
			this.firePropertyChange(PROPERTY_PIPE_OUTPUT_PORTS, null, this.getOutputPorts());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
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
		try {
			if (!this.isInitialized()) {
				this.initialize();
			}
			this.doWork();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        this.busy = false;
	};
	
	protected abstract void doWork();

	@Override
	synchronized public void dispose() {
		this.isInitialized = false;
	}
}
