package com.terpomo.wavy.flow;

import com.terpomo.wavy.core.IWavyModel;
import com.terpomo.wavy.marshal.IMarshallable;
import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Project implements IWavyModel, IMarshallable {

	private static final Logger LOGGER = Logger.getLogger(Project.class.getName());
	private String name;
	private List<IPipe> pipes;
	private Map<String, IPipe> nameToPipeMap;
	
	public Project() {
		this.name = "";
		this.pipes = new ArrayList<IPipe>();
		this.nameToPipeMap = new HashMap<>();
	}

	@MarshalAttr(attrName = MarshallingKeys.KEY_NAME)
	public String getName() {
		return name;
	}

	@MarshalAttr(attrName = MarshallingKeys.KEY_NAME)
	public void setName(String name) {
		this.name = name;
	}

	public List<IPipe> getPipes() {
		return pipes;
	}

	public synchronized void addPipe(IPipe p) {
		if (!this.getPipes().contains(p)) {
			ArrayList<IPipe> newPipes = new ArrayList<IPipe>(this.getPipes());
			newPipes.add(p);
			this.setPipes(newPipes);
		}
	}

	public synchronized void removePipe(IPipe pipe) {
		ArrayList<IPipe> newPipes = new ArrayList<IPipe>(this.getPipes());
		newPipes.remove(pipe);
		this.setPipes(newPipes);
	}

	private void setPipes(List<IPipe> pipes) {
		this.pipes = pipes;
		this.rebuildNameToPipeMap(this.pipes);
	}

	public IPipe getPipeByName(String name) {
		return this.nameToPipeMap.get(name);
	}

	private void rebuildNameToPipeMap(List<IPipe> pipes) {
		Map<String, IPipe> map = new HashMap<>();
		for (IPipe pipe : pipes) {
			if (map.containsKey(pipe.getName()))
				LOGGER.warning(String.format("Pipe name conflict within project %s. Pipe name %s", this.getName(), pipe.getName()));
			map.put(pipe.getName(), pipe);
		}
		this.nameToPipeMap = map;
	}
}
