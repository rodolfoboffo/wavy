package com.terpomo.wavy.flow;

import com.terpomo.wavy.core.IObservableObject;
import com.terpomo.wavy.core.IWavyModel;

import java.util.List;

public interface IPipe extends IObservableObject, IWavyModel {
	public List<InputPort> getInputPorts();
	public List<OutputPort> getOutputPorts();
	public void initialize();
	public boolean isInitialized();
	public void process();
	public void dispose();
	public boolean isBusy();
}
