package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.Buffer;
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
	private Mixer.Info mixer;
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
		this.buildInputPorts(this.numOfChannels);
		this.buffers = new Buffer[this.numOfChannels];
		for (int i = 0; i < this.numOfChannels; i++) {
			InputPort p = this.getInputPorts().get(i);
			this.buffers[i] = p.getBuffer();
		}
	}

	synchronized private void buildEncoder() {
		this.audioBufferSize = (int)(this.sampleRate*0.01);
		this.encoder = new LPCMEncoder(this.sampleRate, this.buffers);
	}

	synchronized public void setNumOfChannels(int numOfChannels) {
		this.numOfChannels = numOfChannels;
		this.dispose();
		this.buildPipes();
		this.buildEncoder();
	}

	public int getNumOfChannels() {
		return numOfChannels;
	}

	synchronized public void setSampleRate(int sampleRate) {
		boolean _isPlaying = this.isPlaying();
		this.stop();
		this.closeLine();
		this.sampleRate = sampleRate;
		this.buildEncoder();
		this.initialize();
		if (_isPlaying)
			this.play();
	}

	public int getSampleRate() {
		return sampleRate;
	}

	@Override
	synchronized public void initialize() {
		this.startLine();
		super.initialize();
	}

	synchronized private void startLine() {
		AudioFormat format = this.encoder.getAudioFormat();
		DataLine.Info sourceLineInfo = new DataLine.Info(SourceDataLine.class, format);
		boolean isSupported = AudioSystem.isLineSupported(sourceLineInfo);
		if (isSupported) {
			try {
				this.line = (SourceDataLine)AudioSystem.getLine(sourceLineInfo);
				this.line.open();
				this.line.start();
			} catch (LineUnavailableException e1) {
				throw new RuntimeException("Could not open audio line.", e1);
			}
		}
		else {
			throw new RuntimeException("Audio Format not supported.");
		}
	}

	synchronized private void closeLine() {
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

	@Override
	synchronized public void dispose() {
		super.dispose();
		this.stop();
		this.closeLine();
	}
	
	public boolean isPlaying() {
		return this.playing;
	}

	synchronized public void play() {
		if (!this.playing) {
			this.playing = true;
		}
	}

	synchronized public void stop() {
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
				byte[] buffer = this.encoder.encode(this.audioBufferSize, this.buffers);
				this.line.write(buffer, 0, buffer.length);
			}
		}
		else {
			this.dispose();
		}
	}
}
