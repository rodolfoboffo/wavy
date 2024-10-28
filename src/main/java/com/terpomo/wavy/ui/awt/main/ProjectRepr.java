package com.terpomo.wavy.ui.awt.main;

import java.awt.Color;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.flow.Project;
import com.terpomo.wavy.ui.awt.pipes.AbstractPipeRepr;

public class ProjectRepr extends Panel {

	private static final long serialVersionUID = -3136424835205807021L;

	private Project project;
	protected List<? extends AbstractPipeRepr> pipesRepr;
	
	public ProjectRepr(Project project) {
		this.project = project;
		this.pipesRepr = new ArrayList<AbstractPipeRepr>();
		for (IPipe pipe : project.getPipes()) {
			
		}
		
		this.setBackground(Color.WHITE);
	}
	
}
