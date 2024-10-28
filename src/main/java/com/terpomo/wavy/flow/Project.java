package com.terpomo.wavy.flow;

import java.util.ArrayList;
import java.util.List;

public class Project {

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
