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
		synchronized (this.pipes) {
			if (!this.pipes.contains(p)) {
				ArrayList<IPipe> newPipes = new ArrayList<IPipe>(this.pipes);
				newPipes.add(p);
				this.pipes = newPipes;
			}
		}
	}
	
	public List<IPipe> getPipes() {
		synchronized (this.pipes) {
			return this.pipes;
		}
	}
	
	public void shutdown() throws InterruptedException {
		this.isPaused = true;
		this.isActive = false;
		for (StudioWorker w : this.workers) {
			w.join();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Studio s = new Studio();
		int SAMPLE_RATE = 44100;
		ConstantWave cw = new ConstantWave(SAMPLE_RATE, 0.5f);
//		ConstantWave cw2 = new ConstantWave(SAMPLE_RATE, 7);
		SignalSourcePipe sp = new SignalSourcePipe(cw);
//		SignalSourcePipe sp2 = new SignalSourcePipe(cw2);
		AudioPlayerPipe player = new AudioPlayerPipe(1, SAMPLE_RATE);
		player.getInputPipes().get(0).setLinkedPort(sp.getOutputPort());
//		player.getInputPipes().get(1).setLinkedPort(sp2.getOutputPort());
		s.addPipe(sp);
//		s.addPipe(sp2);
		s.addPipe(player);
		Thread.sleep(30000);
		s.shutdown();
	}
}
