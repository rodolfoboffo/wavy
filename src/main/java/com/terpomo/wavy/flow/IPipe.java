package com.terpomo.wavy.flow;

import com.terpomo.wavy.core.IObservableObject;
import com.terpomo.wavy.core.IWavyModel;
import com.terpomo.wavy.util.Dimension;
import com.terpomo.wavy.util.Point;

import java.util.List;

public interface IPipe extends IObservableObject, IWavyModel {
	public List<InputPort> getInputPorts();
	public List<OutputPort> getOutputPorts();
	public List<IPort> getPorts();
	public void initialize();
	public boolean isInitialized();
	public void process();
	public void dispose();
	public void clearCache();
	public boolean isBusy();
	public Point getLocation();
	public void setLocation(Point location);
	public Dimension getDimension();
	public void setDimension(Dimension dimension);
	public String getName();
	public void setName(String name);
}
