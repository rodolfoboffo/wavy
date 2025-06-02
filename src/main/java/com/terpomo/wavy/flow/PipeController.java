package com.terpomo.wavy.flow;

import com.terpomo.wavy.marshal.MarshallerUtil;
import com.terpomo.wavy.marshal.ProjectMarshaller;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PipeController {
	public static int DEFAULT_NUM_OF_WORKERS = 2;
	private final List<Worker> workers;
	private List<Project> projects;
	private boolean isActive = true;
	private boolean isPaused = true;
	
	public PipeController() {
		this(DEFAULT_NUM_OF_WORKERS);
	}
	
	public PipeController(int numOfWorkers) {
		this.projects = new ArrayList<Project>();
		this.workers = new ArrayList<Worker>();
		for (int i = 0; i < numOfWorkers; i++) {
			Worker w = new Worker(this, String.format("Worker %d", i));
			this.workers.add(w);
			w.start();
		}
	}
	
	public List<Project> getProjects() {
		return projects;
	}
	
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public boolean togglePause() {
		synchronized (this) {
			this.isPaused = !this.isPaused;
			if(!this.isPaused) {
				this.notifyWorkers();
			}
			return this.isPaused;
		}
	}

	private void notifyWorkers() {
        for (Worker worker : this.workers) {
            synchronized (worker) {
                worker.notify();
            }
        }
	}
	
	public void addPipe(Project project, IPipe p) {
		project.addPipe(p);
		this.notifyWorkers();
	}

	public Project getProjectFromPipe(IPipe pipe) {
		for (Project project : this.getProjects()) {
			if (project.getPipes().contains(pipe)) {
				return project;
			}
		}
		return null;
	}

	public void removePipe(Project project, IPipe pipe) {
		synchronized (pipe) {
			project.removePipe(pipe);
			for (IPort port : pipe.getInputPorts()) {
				this.unlinkPort(port);
			}
			for (IPort port : pipe.getOutputPorts()) {
				this.unlinkPort(port);
			}
		}
		this.notifyWorkers();
	}
	
	public Project createNewProject(String projectName) {
		Project p;
		p = new Project(projectName);
		this.addProject(p);
		return p;
	}

	private synchronized void addProject(Project project) {
		List<Project> newProjects = new ArrayList<Project>(this.projects);
		newProjects.add(project);
		this.setProjects(newProjects);
		this.notifyWorkers();
	}
	
	public void shutdown() throws InterruptedException {
		this.isPaused = true;
		this.isActive = false;
		this.notifyWorkers();
		for (Worker w : this.workers) {
			w.join();
		}
	}
	
	public void linkPorts(IPort portA, IPort portB) {
		if (((portA instanceof InputPort) && (portB instanceof InputPort)) || ((portA instanceof OutputPort) && (portB instanceof OutputPort))) {
			throw new RuntimeException("Links can be made between Input and Output ports only");
		}
		portA.setLinkedPort(portB);
		this.notifyWorkers();
	}

	public void unlinkPort(IPort port) {
		if (port.getLinkedPort() != null) {
			port.setLinkedPort(null);
		}
	}

	public void clearCache(IPipe pipe) {
		pipe.clearCache();
	}

	public void saveProject(Project project, File file) {
		ProjectMarshaller marshaller = new ProjectMarshaller();
		JSONObject json = marshaller.marshal(project);
        try {
            FileWriter writer = new FileWriter(file);
			json.write(writer);
			writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not save project file.", e);
        }
    }

	public Project openProject(File file) {
        try {
            byte[] byteContent = Files.readAllBytes(file.toPath());
			String content = new String(byteContent);
            JSONObject json = new JSONObject(content);
			Project p = (Project) MarshallerUtil.unmarshal(json);
			this.addProject(p);
			return p;
        } catch (IOException e) {
            throw new RuntimeException("Cannot open file.", e);
        }
	}
}
