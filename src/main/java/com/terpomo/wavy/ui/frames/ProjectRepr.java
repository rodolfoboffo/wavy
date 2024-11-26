package com.terpomo.wavy.ui.frames;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terpomo.wavy.WavyDisposable;
import com.terpomo.wavy.flow.PipeTypeEnum;
import com.terpomo.wavy.flow.Project;
import com.terpomo.wavy.ui.UIController;
import com.terpomo.wavy.ui.components.IWavyRepr;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.util.AbsoluteLayout;

public class ProjectRepr extends Panel implements WavyDisposable, IWavyRepr {

	private static final long serialVersionUID = -3136424835205807021L;

	private Project project;
	private final AbsoluteLayout layout;
	protected final List<AbstractPipeRepr> pipesRepr;
	private final Map<PipeTypeEnum, Integer> pipeTypeCountMap;
	
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
		UIController.getInstance().addModelToReprMapEntry(project, this);
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

	@Override
	public void wavyDispose() {
		for (AbstractPipeRepr pipe : this.pipesRepr) {
			pipe.wavyDispose();
		}
		Container parent = this.getParent();
		if (parent != null) {
			parent.remove(this);
		}
	}
}
