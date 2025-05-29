package com.terpomo.wavy.ui.frames;

import com.terpomo.wavy.IWavyDisposable;
import com.terpomo.wavy.flow.Project;
import com.terpomo.wavy.ui.UIController;
import com.terpomo.wavy.ui.components.IWavyRepr;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.util.AbsoluteLayout;
import com.terpomo.wavy.util.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepr extends Panel implements IWavyDisposable, IWavyRepr {

	private static final long serialVersionUID = -3136424835205807021L;

	private final Project project;
	private final AbsoluteLayout layout;
	protected final List<AbstractPipeRepr<?>> pipesRepr;
	
	public ProjectRepr(Project project) {
		this.project = project;
		this.layout = new AbsoluteLayout();
		this.setLayout(this.layout);
		this.pipesRepr = new ArrayList<AbstractPipeRepr<?>>();
		this.setBackground(Color.WHITE);
		UIController.getInstance().addModelToReprMapEntry(project, this);
	}

	@Override
	public String getName() {
		return this.project.getName();
	}

	public Project getProject() {
		return project;
	}

	public void setOnTop(AbstractPipeRepr<?> pipeRepr) {
		this.setComponentZOrder(pipeRepr, 0);
	}
	
	public void addPipeRepr(AbstractPipeRepr<?> pipeRepr) {
		this.pipesRepr.add(pipeRepr);
		this.add(pipeRepr);
		Point pipeLocation = pipeRepr.getPipe().getLocation();
		if (pipeLocation != null) {
			pipeRepr.setLocation((int)pipeLocation.getX(), (int)pipeLocation.getY());
		}
		this.setOnTop(pipeRepr);
		this.revalidate();
		this.repaint();
	}

	public void removePipeRepr(AbstractPipeRepr<?> pipeRepr) {
		this.pipesRepr.remove(pipeRepr);
		this.remove(pipeRepr);
		this.revalidate();
		this.repaint();
	}

	@Override
	public void wavyDispose() {
		for (AbstractPipeRepr<?> pipe : this.pipesRepr) {
			pipe.wavyDispose();
		}
		Container parent = this.getParent();
		if (parent != null) {
			parent.remove(this);
		}
	}
}
