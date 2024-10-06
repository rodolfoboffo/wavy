package wavy.sound.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wavy.signals.Signal;
import wavy.sound.Encoder;
import wavy.sound.LPCMEncoder;

public class AudioPlayer {
	
	protected Encoder encoder;
	protected List<Signal> signals;
	protected AudioPlayerThread playerThread;
	protected boolean playing = false;
	
	public AudioPlayer(Signal...s) {
		this.signals = Arrays.asList(s);
	}
	
	public AudioPlayer() {
		this.signals = new ArrayList<Signal>();
	}
	
	public boolean isPlaying() {
		return this.playing;
	}
	
	public final Encoder getEncoder() {
		return encoder;
	}

	public void play() {
		synchronized (this) {
			if (!this.playing) {
				this.playing = true;
				this.encoder = new LPCMEncoder((Signal[]) signals.toArray(new Signal[signals.size()]));
				this.playerThread = new AudioPlayerThread(this);
				this.playerThread.start();
			}
		}
	}
	
	public void stop() throws InterruptedException {
		synchronized (this) {
			if (this.playing) {
				this.playing = false;
				this.playerThread.join();
				this.encoder = null;
				this.playerThread = null;
			}
		}
	}
	
}
