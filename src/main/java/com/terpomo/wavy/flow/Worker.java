package com.terpomo.wavy.flow;

public class Worker extends Thread {

	protected PipeController controller;
	private static final int SLEEP_TIME_MILLIS = 100;
	
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
				try {
					Thread.sleep(SLEEP_TIME_MILLIS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
