package com.terpomo.wavy.flow;

public class Worker extends Thread {

	protected Controller controller;
	
	public Worker(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void run() {
		super.run();
		while (this.controller.isActive()) {
			if (!this.controller.isPaused()) {
				for (Project project : this.controller.getProjects()) {
					for (IPipe p : project.getPipes()) {
						if (!p.isBusy())
							p.process();
					}
				}
			}
		}
		System.out.println("Thread finished.");
	}
}
