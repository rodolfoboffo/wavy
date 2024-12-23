package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.Buffer;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.sound.Encoder;
import com.terpomo.wavy.sound.LPCMEncoder;

import javax.sound.sampled.*;
import java.util.Queue;

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
		this.buffers = new Buffer[this.numOfChannels];
		this.sampleRate = sampleRate;
		for (int i = 0; i < numOfChannels; i++) {
			InputPort p = new InputPort(this);
			this.getInputPorts().add(p);
			this.buffers[i] = p.getBuffer();
		}
		this.audioBufferSize = (int)(this.sampleRate*0.001);
		this.encoder = new LPCMEncoder(this.sampleRate, this.buffers);
	}
	
	@Override
	public void initialize() {
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
	public void dispose() {
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
	
	public void play() {
		synchronized (this) {
			if (!this.playing) {
				this.playing = true;
			}
		}
	}
	
	public void stop() throws InterruptedException {
		synchronized (this) {
			if (this.playing) {
				this.playing = false;
			}
		}
	}

	protected int numOfFramesAvailable() {
		int frames = Integer.MAX_VALUE;
		for (Buffer b : this.buffers) {
			int bufferSize = b.getSize();
			frames = bufferSize < frames ? bufferSize : frames;
		}
		return frames;
	}
	
	@Override
	public void doWork() {
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
