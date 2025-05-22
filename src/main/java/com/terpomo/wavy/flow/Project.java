package com.terpomo.wavy.flow;

import com.terpomo.wavy.core.IWavyModel;
import com.terpomo.wavy.marshal.IMarshallable;

import java.util.ArrayList;
import java.util.List;

public class Project implements IWavyModel, IMarshallable {

	protected List<IPipe> pipes;
	
	public Project() {
		this.pipes = new ArrayList<IPipe>();
	}
	
	public List<IPipe> getPipes() {
		return pipes;
	}
	
	public void setPipes(List<IPipe> pipes) {
		this.pipes = pipes;
	}
}
