package com.terpomo.wavy.flow;

import java.util.ArrayList;
import java.util.List;

public class PipeController {
	public static int DEFAULT_NUM_OF_WORKERS = 2;
	private List<Worker> workers;
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
		synchronized (project.getPipes()) {
			if (!project.getPipes().contains(p)) {
				ArrayList<IPipe> newPipes = new ArrayList<IPipe>(project.getPipes());
				newPipes.add(p);
				project.setPipes(newPipes);
			}
		}
		this.notifyWorkers();
	}
	
	public Project createNewProject() {
		Project p;
		synchronized (this) {
			p = new Project();
			List<Project> newProjects = new ArrayList<Project>(this.projects);
			newProjects.add(p);
			this.setProjects(newProjects);
		}
		this.notifyWorkers();
		return p;
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
}
