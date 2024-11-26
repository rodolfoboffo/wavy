package com.terpomo.wavy.ui;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terpomo.wavy.IWavyDisposable;
import com.terpomo.wavy.core.IWavyModel;
import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.flow.PipeController;
import com.terpomo.wavy.flow.PipeTypeEnum;
import com.terpomo.wavy.flow.Project;
import com.terpomo.wavy.ui.components.IWavyRepr;
import com.terpomo.wavy.ui.frames.ProjectRepr;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipeReprFactory;
import com.terpomo.wavy.ui.pipes.PortRepr;

public class UIController extends Component implements IWavyDisposable {

	private static final long serialVersionUID = 2929056922201884170L;
	private final static UIController instance = new UIController();
	public static final String PROPERTY_SELECTED_PROJECT = "PROPERTY_SELECTED_PROJECT";
	public static final String PROPERTY_IS_PAUSED = "PROPERTY_IS_PAUSED";
	private final List<ProjectRepr> projectsRepr;
	private ProjectRepr selectedProjectRepr;
	private PortRepr selectedPort;
	private final PipeController controller;
	private final Map<IWavyModel, IWavyRepr> modelToReprMap;
	
	private UIController() {
		this.controller = new PipeController();
		this.projectsRepr = new ArrayList<ProjectRepr>();
		this.modelToReprMap = new HashMap<>();
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

	public void addModelToReprMapEntry(IWavyModel modelObj, IWavyRepr reprObj) {
		this.modelToReprMap.put(modelObj, reprObj);
	}

	public IWavyRepr getReprFromModelObj(IWavyModel modelObj) {
		return this.modelToReprMap.get(modelObj);
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
}
