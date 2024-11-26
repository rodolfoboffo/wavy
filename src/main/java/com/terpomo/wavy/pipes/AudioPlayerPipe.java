package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.Buffer;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.sound.Encoder;
import com.terpomo.wavy.sound.LPCMEncoder;

import javax.sound.sampled.*;

public class AudioPlayerPipe extends AbstractPipe {
	
	public static final int DEFAULT_NUM_CHANNELS = 1;
	protected int sampleRate;
	protected int numOfChannels;
	protected int audioBufferSize;
	protected boolean playing = true;
	protected Encoder encoder;
	protected Buffer[] buffers;
	protected SourceDataLine line;
	
	public AudioPlayerPipe() {
		this(DEFAULT_NUM_CHANNELS, Constants.DEFAULT_SAMPLE_RATE);
	}
	
	public AudioPlayerPipe(int numOfChannels, int sampleRate) {
		this.numOfChannels = numOfChannels;
		this.sampleRate = sampleRate;
		this.buildPipes();
		this.buildEncoder();
	}

	synchronized private void buildPipes() {
		this.dispose();
		this.buffers = new Buffer[this.numOfChannels];
		for (IPort port : this.getInputPorts()) {
			port.wavyDispose();
		}
		this.getInputPorts().clear();
		for (int i = 0; i < this.numOfChannels; i++) {
			InputPort p = new InputPort(this);
			this.getInputPorts().add(p);
			this.buffers[i] = p.getBuffer();
		}
		this.firePropertyChange(PROPERTY_PIPE_INPUT_PORTS, null, this.getInputPorts());
	}

	synchronized private void buildEncoder() {
		this.dispose();
		this.audioBufferSize = (int)(this.sampleRate*0.01);
		this.encoder = new LPCMEncoder(this.sampleRate, this.buffers);
	}

	synchronized public void setNumOfChannels(int numOfChannels) {
		this.numOfChannels = numOfChannels;
		this.buildPipes();
	}

	public int getNumOfChannels() {
		return numOfChannels;
	}

	synchronized public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
		this.buildEncoder();
	}

	public int getSampleRate() {
		return sampleRate;
	}

	@Override
	synchronized public void initialize() {
		AudioFormat format = this.encoder.getAudioFormat();
		DataLine.Info sourceLineInfo = new DataLine.Info(SourceDataLine.class, format);
		boolean isSupported = AudioSystem.isLineSupported(sourceLineInfo);
		if (isSupported) {
			try {
				this.line = (SourceDataLine)AudioSystem.getLine(sourceLineInfo);
				this.line.open();
				this.line.start();
				super.initialize();
			} catch (LineUnavailableException e1) {
				throw new RuntimeException("Could not open audio line.", e1);
			}
		}
		else {
			throw new RuntimeException("Audio Format not supported.");
		}
	}
	
	@Override
	synchronized public void dispose() {
		super.dispose();
		if (this.line != null) {
			this.line.flush();
			if (this.line.isActive()) {
				this.line.stop();
			}
			if (this.line.isOpen()) {
				this.line.close();
			}
			this.line = null;
		}
	}
	
	public boolean isPlaying() {
		return this.playing;
	}

	synchronized public void play() {
		if (!this.playing) {
			this.playing = true;
		}
	}

	synchronized public void stop() throws InterruptedException {
		if (this.playing) {
			this.playing = false;
		}
	}

	synchronized protected int numOfFramesAvailable() {
		int frames = Integer.MAX_VALUE;
		for (Buffer b : this.buffers) {
			int bufferSize = b.getSize();
			frames = Math.min(bufferSize, frames);
		}
		return frames;
	}
	
	@Override
	synchronized public void doWork() {
		if (this.isPlaying()) {
			if (this.line.available() >= this.audioBufferSize && this.numOfFramesAvailable() >= this.audioBufferSize) {
				byte[] buffer = this.encoder.getNumOfFrames(this.audioBufferSize);
				this.line.write(buffer, 0, buffer.length);
			}
		}
		else {
			this.dispose();
		}
	}
}
