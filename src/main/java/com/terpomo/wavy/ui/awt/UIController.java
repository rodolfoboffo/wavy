package com.terpomo.wavy.ui.awt;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import com.terpomo.wavy.flow.Controller;
import com.terpomo.wavy.flow.Project;
import com.terpomo.wavy.ui.awt.main.ProjectRepr;

public class UIController extends Component {

	private static final long serialVersionUID = 2929056922201884170L;
	private final static UIController instance = new UIController();
	public static final String PROPERTY_SELECTED_PROJECT = "PROPERTY_SELECTED_PROJECT";
	private List<ProjectRepr> projectsRepr;
	private ProjectRepr selectedProject;
	private Controller controller;
	
	private UIController() {
		this.controller = new Controller();
		this.projectsRepr = new ArrayList<ProjectRepr>();
	}
	
	public static UIController getInstance() {
		return instance;
	}
	
	public ProjectRepr getSelectedProject() {
		return selectedProject;
	}
	
	public void exit() {
		try {
			this.controller.shutdown();
			System.exit(0);
		} catch (InterruptedException e) {
			System.exit(-1);
		}
	}

	public void setSelectedProject(ProjectRepr selectedProject) {
		ProjectRepr oldValue = this.selectedProject;
		if (oldValue != selectedProject) {
			this.selectedProject = selectedProject;
			this.firePropertyChange(PROPERTY_SELECTED_PROJECT, oldValue, selectedProject);
		}
	}
	
	public void onSelectedProjectChanged(PropertyChangeListener l) {
		this.addPropertyChangeListener(PROPERTY_SELECTED_PROJECT, l);
	}
	
	public ProjectRepr createNewProject() {
		Project p = this.controller.createNewProject();
		ProjectRepr pRepr = new ProjectRepr(p);
		this.projectsRepr.add(pRepr);
		this.setSelectedProject(pRepr);
		return pRepr;
	}
	
}
