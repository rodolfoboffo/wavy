package com.terpomo.wavy.flow;

public class Worker extends Thread {

	protected PipeController controller;
	
	public Worker(PipeController controller) {
		this.controller = controller;
	}
	
	public Worker(PipeController controller, String name) {
		this(controller);
		this.setName(name);
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
			else {
				synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
			}
		}
	}
}
