package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.Buffer;
import com.terpomo.wavy.oscilloscope.TimeValuePair;
import com.terpomo.wavy.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class OscilloscopePipe extends AbstractPipe {

	private static final int DEFAULT_POINT_SKIP = 30;
	private static final int DEFAULT_NUMBER_OF_CHANNELS = 2;
	private static final float MAX_SCALE = 3.0f;
	private static final float DEFAULT_SCALE = 1.0f;
	private List<Buffer> buffers;
	private int sampleRate;
	private int numberOfChannels;
	private long timestamp;
	private float scale;
	private int pointSkip;
	
	public OscilloscopePipe(float scale) {
		this.numberOfChannels = DEFAULT_NUMBER_OF_CHANNELS;
		this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
		this.scale = scale;
		this.pointSkip = DEFAULT_POINT_SKIP;
		this.buffers = new ArrayList<>();
		this.buildPipesAndBuffers();
	}

	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	synchronized private void buildPipesAndBuffers() {
        try {
			this.buildInputPipes(this.numberOfChannels);
            this.buffers = ListUtils.buildNewList(this.numberOfChannels, Buffer.class, this.buffers, Buffer.class.getDeclaredConstructor(int.class, boolean.class), new Object[]{(int)(this.sampleRate*this.scale), true});
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

	public void setNumberOfChannels(int numberOfChannels) {
		this.numberOfChannels = numberOfChannels;
		this.buildPipesAndBuffers();
	}

	synchronized public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
		for(Buffer b : this.buffers) {
			b.resizeBuffer((int) (this.sampleRate * this.scale));
		}
	}

	public OscilloscopePipe() {
		this(DEFAULT_SCALE);
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
		this.timestamp = now;
		int samplesToFetch = (int)(1.0f / 1000 * intervalMillis * this.sampleRate);
		int maxAvailableSamples = samplesToFetch;
		for (int i = 0; i < this.numberOfChannels; i++) {
			maxAvailableSamples = Math.min(this.getInputPorts().get(i).getBuffer().getSize(), maxAvailableSamples);
		}
		for (int i = 0; i < this.numberOfChannels; i++) {
			this.buffers.get(i).putAll(this.getInputPorts().get(i).getBuffer().fetch(maxAvailableSamples));
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
		for (int i = 0; i < this.numberOfChannels; i++) {
			this.buffers.get(i).resizeBuffer((int)(newScale*this.sampleRate));
		}
	}

	synchronized private ArrayList<TimeValuePair> generateTimeValuePairs(Buffer buffer) {
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
}
