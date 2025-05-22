package com.terpomo.wavy.pipes.monitors;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.SignalBuffer;
import com.terpomo.wavy.oscilloscope.TimeValuePair;
import com.terpomo.wavy.util.ListUtils;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class OscilloscopePipe extends AbstractPipe {

	private static final int DEFAULT_POINT_SKIP = 30;
	private static final int DEFAULT_NUMBER_OF_CHANNELS = 1;
	private static final float MAX_SCALE = 3.0f;
	private static final float DEFAULT_SCALE = 1.0f;
	private List<SignalBuffer> buffers;
	private int bufferSize;
	private int sampleRate;
	private int numberOfChannels;
	private long timestamp;
	private float scale;
	private int pointSkip;
	
	public OscilloscopePipe(String pipeName, float scale) {
		super(pipeName);
		this.numberOfChannels = DEFAULT_NUMBER_OF_CHANNELS;
		this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
		this.scale = scale;
		this.bufferSize = (int)(this.sampleRate*this.scale);
		this.pointSkip = DEFAULT_POINT_SKIP;
		this.buffers = new ArrayList<>();
		this.buildPortsAndBuffers();
	}

	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	synchronized private void buildPortsAndBuffers() {
        try {
			this.dispose();
			this.buildInputPorts(this.numberOfChannels);
            this.buffers = ListUtils.buildNewList(this.numberOfChannels, SignalBuffer.class, this.buffers, SignalBuffer.class.getDeclaredConstructor(int.class, boolean.class), new Object[]{this.bufferSize, true});
			this.resizeAndClearBuffers();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

	synchronized public void setNumberOfChannels(int numberOfChannels) {
		this.numberOfChannels = numberOfChannels;
		this.buildPortsAndBuffers();
	}

	synchronized private void resizeAndClearBuffers() {
		for(SignalBuffer b : this.buffers) {
			b.resizeBuffer(this.bufferSize);
			b.clear();
		}
		for (InputPort p : this.getInputPorts()) {
			p.getBuffer().resizeBuffer(this.bufferSize);
			p.getBuffer().clear();
		}
	}

	synchronized public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
		this.bufferSize = (int)(this.sampleRate*this.scale);
		this.resizeAndClearBuffers();
	}

	public OscilloscopePipe(String pipeName) {
		this(pipeName, DEFAULT_SCALE);
	}

	@Override
	public void initialize() {
		this.timestamp = System.currentTimeMillis();
		super.initialize();
	}

	@Override
	synchronized protected void doWork() {
		long now = System.currentTimeMillis();
		long intervalMillis = now - this.timestamp;
		int samplesToFetch = (int)(1.0f / 1000 * intervalMillis * this.sampleRate);
		if (samplesToFetch > 0) {
			this.timestamp = now;
			int maxAvailableSamples = samplesToFetch;
			for (int i = 0; i < this.numberOfChannels; i++) {
				maxAvailableSamples = Math.min(this.getInputPorts().get(i).getBuffer().getSize(), maxAvailableSamples);
			}
			for (int i = 0; i < this.numberOfChannels; i++) {
				this.buffers.get(i).putAll(this.getInputPorts().get(i).getBuffer().fetch(maxAvailableSamples));
			}
		}
	}

	public int getPointSkip() {
		return pointSkip;
	}

	synchronized public void setPointSkip(int pointSkip) {
		int newPointSkip = Math.max(pointSkip, 0);
		this.pointSkip = newPointSkip;
	}

	synchronized public void setQuality(float quality) {
        float newQuality = Math.max(Math.min(quality, 100f), 0.05f);
        int newPointSkip = Math.max((int) (100f / newQuality - 1), 0);
        this.setPointSkip(newPointSkip);
    }

	synchronized public float getQuality() {
		return 100f / (this.pointSkip + 1);
	}

	public float getScale() {
		return scale;
	}

	synchronized public void setScale(float scale) {
		float newScale = Math.min(scale, MAX_SCALE);
		this.scale = scale;
		this.bufferSize = (int)(this.sampleRate*this.scale);
		this.resizeAndClearBuffers();
	}

	synchronized private ArrayList<TimeValuePair> generateTimeValuePairs(SignalBuffer buffer) {
		ArrayList<TimeValuePair> pairs = new ArrayList<>();
		Float[] clonedBuffer = buffer.getAll();
		for (int i = 0; i < clonedBuffer.length; i += this.pointSkip+1) {
			pairs.add(new TimeValuePair((float)i/this.sampleRate, clonedBuffer[i]));
		}
		return pairs;
	}

	synchronized public List<TimeValuePair> getValuesForChannel(int channelIndex) {
		ArrayList<TimeValuePair> values = this.generateTimeValuePairs(this.buffers.get(channelIndex));
		return values;
	}

	@Override
	public synchronized void clearCache() {
		super.clearCache();
		for (SignalBuffer b : this.buffers) {
			b.clear();
		}
	}
}
