package com.terpomo.wavy.flow;

import java.util.ArrayList;
import java.util.List;

public class Controller {
	public static int DEFAULT_NUM_OF_WORKERS = 1;
	private List<Worker> workers;
	private List<Project> projects;
	private boolean isActive = true;
	private boolean isPaused = false;
	
	public Controller() {
		this(DEFAULT_NUM_OF_WORKERS);
	}
	
	public Controller(int numOfWorkers) {
		this.projects = new ArrayList<Project>();
		this.workers = new ArrayList<Worker>();
		for (int i = 0; i < numOfWorkers; i++) {
			Worker w = new Worker(this);
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
	
	public void addPipe(Project project, IPipe p) {
		synchronized (project.getPipes()) {
			if (!project.getPipes().contains(p)) {
				ArrayList<IPipe> newPipes = new ArrayList<IPipe>(project.getPipes());
				newPipes.add(p);
				project.setPipes(newPipes);
			}
		}
	}
	
	public Project createNewProject() {
		synchronized (this) {
			Project p = new Project();
			List<Project> newProjects = new ArrayList<Project>(this.projects);
			newProjects.add(p);
			this.setProjects(newProjects);
			return p;
		}
	}
	
	public void shutdown() throws InterruptedException {
		this.isPaused = true;
		this.isActive = false;
		for (Worker w : this.workers) {
			w.join();
		}
	}
}
