package wavy.flow;

import java.util.ArrayList;
import java.util.List;

import wavy.signals.ConstantWave;
import wavy.signals.SignalSourcePipe;
import wavy.sound.player.AudioPlayerPipe;

public class Studio {
	public static int DEFAULT_NUM_OF_WORKERS = 1;
	protected List<IPipe> pipes;
	protected List<StudioWorker> workers;
	protected boolean isActive = true;
	protected boolean isPaused = false;
	
	public Studio() {
		this(DEFAULT_NUM_OF_WORKERS);
	}
	
	public Studio(int numOfWorkers) {
		this.workers = new ArrayList<StudioWorker>();
		this.pipes = new ArrayList<IPipe>();
		for (int i = 0; i < numOfWorkers; i++) {
			StudioWorker w = new StudioWorker(this);
			this.workers.add(w);
			w.start();
		}
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public void addPipe(IPipe p) {
		if (!this.pipes.contains(p)) {
			this.pipes.add(p);
		}
	}
	
	public List<IPipe> getPipes() {
		return pipes;
	}
	
	public synchronized void shutdown() throws InterruptedException {
		this.isActive = false;
		for (StudioWorker w : this.workers) {
			w.join();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Studio s = new Studio();
		int SAMPLE_RATE = 44100;
		ConstantWave cw = new ConstantWave(SAMPLE_RATE, 400);
		SignalSourcePipe sp = new SignalSourcePipe(cw);
		AudioPlayerPipe player = new AudioPlayerPipe(1, SAMPLE_RATE);
		player.getInputPipes().get(0).setLinkedPort(sp.getOutputPort());
		s.addPipe(sp);
		s.addPipe(player);
	}
}
