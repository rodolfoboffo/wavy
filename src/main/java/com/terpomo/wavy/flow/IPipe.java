package com.terpomo.wavy.flow;

import java.util.List;

public interface IPipe {
	public List<InputPort> getInputPorts();
	public List<OutputPort> getOutputPorts();
	public void initialize();
	public boolean isInitialized();
	public void process();
	public void dispose();
	public boolean isBusy();
}
