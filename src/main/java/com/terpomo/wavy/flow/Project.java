package com.terpomo.wavy.flow;

import com.terpomo.wavy.core.IWavyModel;
import com.terpomo.wavy.marshal.IMarshallable;

import java.util.ArrayList;
import java.util.List;

public class Project implements IWavyModel, IMarshallable {

	private String name;
	private List<IPipe> pipes;
	
	public Project(String name) {
		this.name = name;
		this.pipes = new ArrayList<IPipe>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IPipe> getPipes() {
		return pipes;
	}
	
	public void setPipes(List<IPipe> pipes) {
		this.pipes = pipes;
	}
}
