package com.terpomo.wavy.ui.awt;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import com.terpomo.wavy.flow.PipeController;
import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.flow.PipeTypeEnum;
import com.terpomo.wavy.flow.Project;
import com.terpomo.wavy.ui.awt.main.ProjectRepr;
import com.terpomo.wavy.ui.awt.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.awt.pipes.DataFlowRepr;
import com.terpomo.wavy.ui.awt.pipes.PipeReprFactory;
import com.terpomo.wavy.ui.awt.pipes.PortRepr;

public class UIController extends Component {

	private static final long serialVersionUID = 2929056922201884170L;
	private final static UIController instance = new UIController();
	public static final String PROPERTY_SELECTED_PROJECT = "PROPERTY_SELECTED_PROJECT";
	private List<ProjectRepr> projectsRepr;
	private ProjectRepr selectedProjectRepr;
	
	private PipeController controller;
	
	private UIController() {
		this.controller = new PipeController();
		this.projectsRepr = new ArrayList<ProjectRepr>();
	}
	
	public static UIController getInstance() {
		return instance;
	}
	
	public void exit() {
		try {
			this.controller.shutdown();
			System.exit(0);
		} catch (InterruptedException e) {
			System.exit(-1);
		}
	}
	
	public ProjectRepr getSelectedProjectRepr() {
		return selectedProjectRepr;
	}
	
	public void setSelectedProjectRepr(ProjectRepr selectedProject) {
		ProjectRepr oldValue = this.selectedProjectRepr;
		if (oldValue != selectedProject) {
			this.selectedProjectRepr = selectedProject;
			this.firePropertyChange(PROPERTY_SELECTED_PROJECT, oldValue, selectedProject);
		}
	}
	
	public void onSelectedProjectChanged(PropertyChangeListener l) {
		this.addPropertyChangeListener(PROPERTY_SELECTED_PROJECT, l);
	}
	
	public ProjectRepr createNewProjectRepr() {
		Project p = this.controller.createNewProject();
		ProjectRepr pRepr = new ProjectRepr();
		pRepr.setProject(p);
		this.projectsRepr.add(pRepr);
		this.setSelectedProjectRepr(pRepr);
		return pRepr;
	}
	
	public void createPipeRepr(PipeTypeEnum pipeType) {
		if (this.selectedProjectRepr != null) {
			Project project = this.selectedProjectRepr.getProject();
			String pipeName = this.selectedProjectRepr.generatePipeName(pipeType);
			AbstractPipeRepr pipeRepr = PipeReprFactory.createPipeRepr(pipeType, pipeName);
			IPipe pipe = pipeRepr.getPipe();
			this.controller.addPipe(project, pipe);
			this.selectedProjectRepr.addPipeRepr(pipeRepr);
		}
	}
	
}
