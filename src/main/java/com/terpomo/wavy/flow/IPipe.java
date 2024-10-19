package com.terpomo.wavy.flow;

import java.util.List;

public interface IPipe {
	public List<IPort> getInputPipes();
	public List<IPort> getOutputPipes();
	public void initialize();
	public void process();
	public void dispose();
	public boolean isBusy();
}
