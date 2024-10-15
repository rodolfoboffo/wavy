package wavy.flow;

public class StudioWorker extends Thread {

	protected Studio studio;
	
	public StudioWorker(Studio studio) {
		this.studio = studio;
	}
	
	@Override
	public void run() {
		super.run();
		while (this.studio.isActive()) {
			if (!this.studio.isPaused()) {
				for (IPipe p : this.studio.getPipes()) {
					if (!p.isBusy())
						p.process();
				}
			}
		}
	}
}
