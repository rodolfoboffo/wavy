package com.terpomo.wavy.pipes.monitors;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.SignalBuffer;
import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;
import com.terpomo.wavy.math.FFT;
import com.terpomo.wavy.math.Utils;
import com.terpomo.wavy.util.ListUtils;
import com.terpomo.wavy.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FFTPipe extends AbstractPipe {

	private static final Logger LOGGER = Logger.getLogger(AbstractPipe.class.getName());
	private static final int MAX_RESOLUTION = 20480;
	public static final int DEFAULT_NUMBER_OF_CHANNELS = 1;
	public static final int DEFAULT_RESOLUTION = 2048;
	public static final int DEFAULT_SAMPLE_RATE = Constants.DEFAULT_SAMPLE_RATE;
	private int numberOfChannels;
	private int resolution;
	private int sampleRate;
	private long timestamp;
	private List<SignalBuffer> buffers;

	public FFTPipe() {
		super();
		this.numberOfChannels = 1;
		this.resolution = DEFAULT_RESOLUTION;
		this.sampleRate = DEFAULT_SAMPLE_RATE;
		this.buffers = new ArrayList<>();
		this.buildPortsAndBuffers();
	}

	synchronized private void buildPortsAndBuffers() {
		try {
			this.dispose();
			this.buffers = ListUtils.buildNewList(this.numberOfChannels, SignalBuffer.class, this.buffers, SignalBuffer.class.getDeclaredConstructor(int.class, boolean.class), new Object[]{(int)(this.MAX_RESOLUTION), true});
			this.buildInputPorts(this.numberOfChannels);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
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

	private List<Point> getPointsFromFFTResult(Float[] result, int sampleRate) {
		ArrayList<Point> values = new ArrayList<>();
		float step = 1.0f * sampleRate / result.length;
		for (int i = 0; i < result.length; i++) {
			values.add(new Point(step * i, result[i]));
		}
		return values;
	}

	synchronized public List<Point> getValuesForChannel(int channelIndex) {
		try {
			if (this.buffers.get(channelIndex).getSize() >= this.resolution) {
				Float[] samples = this.buffers.get(channelIndex).fetch(this.resolution);
				if (samples.length > 0) {
					Float[] result = FFT.fft(samples);
					List<Point> points = this.getPointsFromFFTResult(result, this.sampleRate);
					return points;
				}
			}
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.WARNING, String.format("Could not calculate fft for channel %d.", channelIndex+1), e);
		}
		return null;
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_NUM_CHANNELS)
	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_NUM_CHANNELS)
	synchronized public void setNumberOfChannels(int numberOfChannels) {
		this.numberOfChannels = numberOfChannels;
		this.buildPortsAndBuffers();
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_RESOLUTION)
	public int getResolution() {
		return resolution;
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_RESOLUTION)
	synchronized public void setResolution(int resolution) {
		int r = Utils.getNearestPowerOfTwo(resolution);
		r = Math.min(r, MAX_RESOLUTION);
		this.resolution = r;
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_SAMPLE_RATE)
	public int getSampleRate() {
		return sampleRate;
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_SAMPLE_RATE)
	synchronized public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}
}
