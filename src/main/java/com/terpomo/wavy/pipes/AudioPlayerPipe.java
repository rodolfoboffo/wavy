package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.SignalBuffer;
import com.terpomo.wavy.sound.AudioUtils;
import com.terpomo.wavy.sound.Encoder;
import com.terpomo.wavy.sound.LPCMEncoder;

import javax.sound.sampled.*;

public class AudioPlayerPipe extends AbstractPipe {
	
	public static final int DEFAULT_NUM_CHANNELS = 1;
	private int sampleRate;
	private int numOfChannels;
	private int audioBufferSize;
	private String mixerName;
	private Encoder encoder;
	private SignalBuffer[] buffers;
	private SourceDataLine line;
	private Mixer.Info mixer;

	public AudioPlayerPipe() {
		this(DEFAULT_NUM_CHANNELS, Constants.DEFAULT_SAMPLE_RATE);
	}
	
	public AudioPlayerPipe(int numOfChannels, int sampleRate) {
		this.numOfChannels = numOfChannels;
		this.sampleRate = sampleRate;
		this.mixerName = null;
		this.mixer = null;
		this.buildPipes();
		this.buildEncoder();
	}

	synchronized private void buildPipes() {
		this.buildInputPorts(this.numOfChannels);
		this.buffers = new SignalBuffer[this.numOfChannels];
		for (int i = 0; i < this.numOfChannels; i++) {
			InputPort p = this.getInputPorts().get(i);
			this.buffers[i] = p.getBuffer();
		}
	}

	synchronized private void buildEncoder() {
		this.audioBufferSize = (int)(this.sampleRate*0.01);
		this.encoder = new LPCMEncoder(this.sampleRate, this.numOfChannels);
	}

	synchronized public void setNumOfChannels(int numOfChannels) {
		this.numOfChannels = numOfChannels;
		this.closeLine();
		this.buildPipes();
		this.buildEncoder();
	}

	public int getNumOfChannels() {
		return numOfChannels;
	}

	synchronized public void setSampleRate(int sampleRate) {
		this.closeLine();
		this.sampleRate = sampleRate;
		this.buildEncoder();
	}

	public int getSampleRate() {
		return sampleRate;
	}

	synchronized private void startLine() {
		if (this.mixer != null && AudioSystem.getMixer(this.mixer).isLineSupported(new DataLine.Info(SourceDataLine.class, this.encoder.getAudioFormat()))) {
			try {
				this.line = AudioSystem.getSourceDataLine(this.encoder.getAudioFormat(), this.mixer);
				this.line.open();
				this.line.start();
			} catch (LineUnavailableException e) {
				throw new RuntimeException("Could not open audio line.", e);
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
		this.closeLine();
	}

	synchronized protected int numOfFramesAvailable() {
		int frames = Integer.MAX_VALUE;
		for (SignalBuffer b : this.buffers) {
			int bufferSize = b.getSize();
			frames = Math.min(bufferSize, frames);
		}
		return frames;
	}
	
	@Override
	synchronized public void doWork() {
		if (this.mixer == null)
			this.resetMixer();
		if (this.line == null)
			this.startLine();
		if (this.line.available() >= this.audioBufferSize && this.numOfFramesAvailable() >= this.audioBufferSize) {
			byte[] buffer = this.encoder.encode(this.audioBufferSize, this.buffers);
			this.line.write(buffer, 0, buffer.length);
		}
	}

	synchronized private void resetMixer() {
		this.mixer = null;
		if (this.mixerName != null) {
			Mixer.Info mixer = AudioUtils.getMixerInfoByName(this.mixerName);
			this.mixer = mixer;
		}
	}

	synchronized public void setMixerName(String name) {
		this.mixerName = name;
		this.resetMixer();
	}

	public String[] getMixerInfos() {
		return AudioUtils.getMixerInfos(SourceDataLine.class, this.encoder.getAudioFormat());
	}
}
