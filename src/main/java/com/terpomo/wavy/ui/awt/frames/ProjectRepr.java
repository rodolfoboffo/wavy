package com.terpomo.wavy.ui.awt.frames;

import java.awt.Color;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terpomo.wavy.flow.PipeTypeEnum;
import com.terpomo.wavy.flow.Project;
import com.terpomo.wavy.ui.awt.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.awt.util.AbsoluteLayout;

public class ProjectRepr extends Panel {

	private static final long serialVersionUID = -3136424835205807021L;
	public static final String PROPERTY_SELECTED_PROJECT = "PROPERTY_SELECTED_PROJECT";
	
	private Project project;
	private AbsoluteLayout layout;
	protected List<AbstractPipeRepr> pipesRepr;
	private Map<PipeTypeEnum, Integer> pipeTypeCountMap;
	
	public ProjectRepr() {
		this.layout = new AbsoluteLayout();
		this.setLayout(this.layout);
		this.pipesRepr = new ArrayList<AbstractPipeRepr>();
		this.pipeTypeCountMap = new HashMap<PipeTypeEnum, Integer>();
		this.setBackground(Color.WHITE);
	}
	
	private Integer getPipeCountAndIncr(PipeTypeEnum pipeType) {
		int c = this.pipeTypeCountMap.getOrDefault(pipeType, 0);
		this.pipeTypeCountMap.put(pipeType, c+1);
		return c;
	}
	
	public String generatePipeName(PipeTypeEnum pipeType) {
		Integer c = this.getPipeCountAndIncr(pipeType);
		String pipeName = String.format("%s %d", pipeType.getFriendlyName(), c);
		return pipeName;
	}
	
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
	public void setOnTop(AbstractPipeRepr pipeRepr) {
		this.setComponentZOrder(pipeRepr, 0);
	}
	
	public void addPipeRepr(AbstractPipeRepr pipeRepr) {
		this.pipesRepr.add(pipeRepr);
		this.add(pipeRepr);
		this.setOnTop(pipeRepr);
		this.revalidate();
		this.repaint();
	}
}
