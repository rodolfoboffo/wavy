package com.terpomo.wavy.flow;

import java.util.List;

public interface IPipe {
	public List<IPort> getInputPorts();
	public List<IPort> getOutputPorts();
	public void initialize();
	public void process();
	public void dispose();
	public boolean isBusy();
}
