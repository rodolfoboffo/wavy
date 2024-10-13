package wavy.sound.player;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import wavy.sound.Encoder;

public class AudioPlayerThread extends Thread {

	private final AudioPlayer player;
	
	public AudioPlayerThread(AudioPlayer player) {
		super();
		this.player = player;
	}
	
	@Override
	public void run() {
		super.run();
		Encoder e = this.player.getEncoder();
		AudioFormat format = e.getAudioFormat();
		DataLine.Info sourceLineInfo = new DataLine.Info(SourceDataLine.class, format);
		Boolean isSupported = AudioSystem.isLineSupported(sourceLineInfo);
		if (isSupported) {
			SourceDataLine line;
			try {
				line = (SourceDataLine)AudioSystem.getLine(sourceLineInfo);
				line.open();
				line.start();
				int bufferSize = (int)(e.getSampleRate()*0.001);
				while(this.player.isPlaying()) {
					if (line.available() >= bufferSize) {
						byte[] buffer = this.player.getEncoder().getNumOfFrames(bufferSize);
						line.write(buffer, 0, buffer.length);
					}
				}
				line.flush();
				line.stop();
			} catch (LineUnavailableException e1) {
				e1.printStackTrace();
			}
		}
	}
}
