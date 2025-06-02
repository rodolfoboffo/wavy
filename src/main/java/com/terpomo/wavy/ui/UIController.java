package com.terpomo.wavy.ui;

import com.terpomo.wavy.IWavyDisposable;
import com.terpomo.wavy.core.IWavyModel;
import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.flow.PipeController;
import com.terpomo.wavy.flow.Project;
import com.terpomo.wavy.pipes.PipeTypeEnum;
import com.terpomo.wavy.ui.components.IWavyRepr;
import com.terpomo.wavy.ui.frames.ProjectRepr;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipeReprFactory;
import com.terpomo.wavy.ui.pipes.PortRepr;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIController extends Component implements IWavyDisposable {

	private static final long serialVersionUID = 2929056922201884170L;
	private final static UIController instance = new UIController();
	public static final String PROPERTY_SELECTED_PROJECT = "PROPERTY_SELECTED_PROJECT";
	public static final String PROPERTY_PROJECTS = "PROPERTY_PROJECTS";
	public static final String PROPERTY_ADDED_PROJECT = "PROPERTY_ADDED_PROJECT";
	public static final String PROPERTY_IS_PAUSED = "PROPERTY_IS_PAUSED";
	private final List<ProjectRepr> projectsRepr;
	private ProjectRepr selectedProjectRepr;
	private PortRepr selectedPort;
	private final PipeController controller;
	private final Map<IWavyModel, IWavyRepr> modelToReprMap;
	private final Map<PipeTypeEnum, Integer> pipeTypeCountMap;
	private int projectCount;
	
	private UIController() {
		this.controller = new PipeController();
		this.projectsRepr = new ArrayList<ProjectRepr>();
		this.modelToReprMap = new HashMap<>();
		this.pipeTypeCountMap = new HashMap<PipeTypeEnum, Integer>();
		this.projectCount = 0;
	}
	
	public static UIController getInstance() {
		return instance;
	}
	
	public void exit() {
		try {
			this.controller.shutdown();
			this.wavyDispose();
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

	public void onProjectsChanged(PropertyChangeListener l) {
		this.addPropertyChangeListener(PROPERTY_PROJECTS, l);
	}

	public void onAddedProjectChanged(PropertyChangeListener l) {
		this.addPropertyChangeListener(PROPERTY_ADDED_PROJECT, l);
	}

	public void addModelToReprMapEntry(IWavyModel modelObj, IWavyRepr reprObj) {
		this.modelToReprMap.put(modelObj, reprObj);
	}

	public void removeModelToReprMapEntry(IWavyModel modelObj) {
		this.modelToReprMap.remove(modelObj);
	}

	public IWavyRepr getReprFromModelObj(IWavyModel modelObj) {
		return this.modelToReprMap.get(modelObj);
	}

	private void addProject(Project project) {
		ProjectRepr projectRepr = new ProjectRepr(project);
		ArrayList<ProjectRepr> oldProjectsList = new ArrayList<>(this.projectsRepr);
		this.projectsRepr.add(projectRepr);
		for (IPipe pipe : project.getPipes()) {
			AbstractPipeRepr<?> pipeRepr = PipeReprFactory.createPipeRepr(PipeTypeEnum.valueOf(pipe.getClass()), pipe);
			projectRepr.addPipeRepr(pipeRepr);
		}
		for (IPipe pipe : project.getPipes()) {
			for (IPort port : pipe.getPorts()) {
				if (port.getLinkedPort() != null) {
					PortRepr portRepr = (PortRepr) this.getReprFromModelObj(port);
					PortRepr linkedPortRepr = (PortRepr) this.getReprFromModelObj(port.getLinkedPort());
					portRepr.setLinkedPortRepr(linkedPortRepr);
				}
			}
		}
		this.firePropertyChange(PROPERTY_ADDED_PROJECT, null, projectRepr);
		this.firePropertyChange(PROPERTY_PROJECTS, oldProjectsList, this.projectsRepr);
		this.setSelectedProjectRepr(projectRepr);
	}

	public void createNewProjectRepr() {
		Project p = this.controller.createNewProject(this.generateProjectName());
		this.addProject(p);
	}

	public void saveSelectedProjectRepr(File fileToSave) {
		ProjectRepr projectRepr = this.getSelectedProjectRepr();
		Project project = projectRepr.getProject();
		this.controller.saveProject(project, fileToSave);
	}

	public void openProjectRepr(File file) {
		Project p = this.controller.openProject(file);
		this.addProject(p);
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

	public String generateProjectName() {
		String projectName = String.format("Project %d", ++this.projectCount);
		return projectName;
	}

	public void createPipeRepr(PipeTypeEnum pipeType) {
		if (this.selectedProjectRepr != null) {
			Project project = this.selectedProjectRepr.getProject();
			String pipeName = this.generatePipeName(pipeType);
			AbstractPipeRepr<?> pipeRepr = PipeReprFactory.createPipeRepr(pipeType, pipeName);
			IPipe pipe = pipeRepr.getPipe();
			this.controller.addPipe(project, pipe);
			this.selectedProjectRepr.addPipeRepr(pipeRepr);
		}
	}

	public void removePipeRepr(AbstractPipeRepr<?> pipeRepr) {
		IPipe pipe = pipeRepr.getPipe();
		Project project = this.controller.getProjectFromPipe(pipe);
		ProjectRepr projectRepr = (ProjectRepr) this.getReprFromModelObj(project);
		projectRepr.removePipeRepr(pipeRepr);
		pipeRepr.wavyDispose();
		this.controller.removePipe(project, pipe);
		pipe.dispose();
	}

	public void onPortClicked(PortRepr portRepr) {
		if (this.selectedPort == null) {
			this.selectPort(portRepr);
		}
		else if (this.selectedPort == portRepr) {
			this.unselectPort();
		}
		else {
			if (portRepr.getLinkedPortRepr() == this.selectedPort) {
				this.unlinkPort(portRepr);
				this.unselectPort();
			}
			else {
				this.unlinkPort(this.selectedPort);
				this.unlinkPort(portRepr);
				this.linkPorts(portRepr, this.selectedPort);
				this.unselectPort();
			}
		}
	}

	private void linkPorts(PortRepr portReprA, PortRepr portReprB) {
		this.controller.linkPorts(portReprA.getPort(), portReprB.getPort());
	}

	private void unlinkPort(PortRepr portRepr) {
		this.controller.unlinkPort(portRepr.getPort());
	}
	
	private void selectPort(PortRepr portRepr) {
		portRepr.setSelected(true);
		this.selectedPort = portRepr;
	}
	
	private void unselectPort() {
		this.selectedPort.setSelected(false);
		this.selectedPort = null;
	}
	
	public boolean isPaused() {
		return this.controller.isPaused();
	}

	public void onPauseToggled(PropertyChangeListener l) {
		this.addPropertyChangeListener(PROPERTY_IS_PAUSED ,l);
	}
	
	public void togglePause() {
		boolean oldValue = this.isPaused();
		boolean newValue = this.controller.togglePause();
		this.firePropertyChange(PROPERTY_IS_PAUSED, oldValue, newValue);
	}

	@Override
	public void wavyDispose() {
		for (ProjectRepr project : this.projectsRepr) {
			project.wavyDispose();
		}
	}

	public void clearCache(AbstractPipeRepr<?> pipeRepr) {
		IPipe pipe = pipeRepr.getPipe();
		this.controller.clearCache(pipe);
		pipeRepr.clearCache();
	}
}
