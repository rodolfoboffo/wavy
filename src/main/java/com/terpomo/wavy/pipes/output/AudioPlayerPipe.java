package com.terpomo.wavy.pipes.output;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.SignalBuffer;
import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;
import com.terpomo.wavy.sound.AudioUtils;
import com.terpomo.wavy.sound.Encoder;
import com.terpomo.wavy.sound.LPCMEncoder;

import javax.sound.sampled.*;

public class AudioPlayerPipe extends AbstractPipe {
	
	public static final int DEFAULT_SAMPLE_RATE = Constants.DEFAULT_SAMPLE_RATE;
	public static final int DEFAULT_NUM_CHANNELS = 1;
	public static final float DEFAULT_VOLUME = 100f;
	private int sampleRate;
	private float volume;
	private int numOfChannels;
	private String mixerName;
	private Encoder encoder;
	private SignalBuffer[] buffers;
	private SourceDataLine line;
	private Mixer.Info mixer;
	private int lineBufferSize;

	public AudioPlayerPipe() {
		super();
		this.volume = DEFAULT_VOLUME;
		this.numOfChannels = DEFAULT_NUM_CHANNELS;
		this.sampleRate = DEFAULT_SAMPLE_RATE;
		this.mixerName = null;
		this.mixer = null;
		this.lineBufferSize = 0;
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
		this.encoder = new LPCMEncoder(this.sampleRate, this.numOfChannels);
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_NUM_CHANNELS)
	synchronized public void setNumOfChannels(int numOfChannels) {
		this.numOfChannels = numOfChannels;
		this.closeLine();
		this.buildPipes();
		this.buildEncoder();
		this.resetMixer();
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_NUM_CHANNELS)
	public int getNumOfChannels() {
		return numOfChannels;
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_SAMPLE_RATE)
	synchronized public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
		this.closeLine();
		this.buildEncoder();
		this.resetMixer();
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_SAMPLE_RATE)
	public int getSampleRate() {
		return sampleRate;
	}

	synchronized private void startLine() {
		if (this.mixer != null && AudioSystem.getMixer(this.mixer).isLineSupported(new DataLine.Info(SourceDataLine.class, this.encoder.getAudioFormat()))) {
			try {
				this.line = AudioSystem.getSourceDataLine(this.encoder.getAudioFormat(), this.mixer);
				this.line.open();
				this.line.start();
				this.lineBufferSize = this.line.getBufferSize();
			} catch (LineUnavailableException e) {
				this.line = null;
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
		if (this.line != null) {
			int bytesPerFrame = this.encoder.getBytesPerFrame();
			int framesToRead = this.numOfFramesAvailable();
			int bytesToRead = framesToRead * bytesPerFrame;
			if (bytesToRead >= this.lineBufferSize * 0.2) {
				int bytesAvailbaleToWrite = this.line.available();
				framesToRead = Math.min(framesToRead, bytesAvailbaleToWrite / bytesPerFrame);
				byte[] buffer = this.encoder.encode(framesToRead, this.buffers, this.volume/100f);
				this.line.write(buffer, 0, buffer.length);
			}
		}
	}

	synchronized private void resetMixer() {
		this.mixer = null;
		if (this.mixerName != null) {
			Mixer.Info mixer = AudioUtils.getMixerInfoByName(this.mixerName);
			this.mixer = mixer;
		}
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_MIXER_NAME)
	synchronized public void setMixerName(String name) {
		this.mixerName = name;
		this.resetMixer();
		this.closeLine();
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_MIXER_NAME)
	public String getMixerName() {
		return mixerName;
	}

	public String[] getMixerInfos() {
		return AudioUtils.getMixerInfos(SourceDataLine.class, this.encoder.getAudioFormat());
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_VOLUME)
	public synchronized void setVolume(float volume) {
		this.volume = Math.max(Math.min(volume, 100), 0);
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_VOLUME)
	public float getVolume() {
		return volume;
	}
}
