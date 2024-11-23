package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.Buffer;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.oscilloscope.TimeValuePair;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;

public class OscilloscopePipe extends AbstractPipe {

	private static final int MIN_POINT_SKIP = 30;
	private static final float MAX_SCALE = 3.0f;
	private static final float DEFAULT_SCALE = 1.0f;
	private final InputPort inputPort;
	private final Buffer buffer;
	private final int sampleRate;
	private long timestamp;
	private float scale;
	private int pointSkip;
	
	public OscilloscopePipe(float scale) {
		this.inputPort = new InputPort(this);
		this.getInputPorts().add(this.inputPort);
		this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
		this.scale = scale;
		this.pointSkip = MIN_POINT_SKIP;
		this.buffer = new Buffer((int)(Constants.DEFAULT_SAMPLE_RATE*this.scale), true);
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
		this.buffer.putAll(this.inputPort.getBuffer().fetch(samplesToFetch));
	}

	private ArrayList<TimeValuePair> generateTimeValuePairs(Buffer buffer) {
		ArrayList<TimeValuePair> pairs = new ArrayList<>();
		Float[] clonedBuffer = buffer.getAll();
		for (int i = 0; i < clonedBuffer.length; i += this.pointSkip+1) {
			pairs.add(new TimeValuePair((float)i/this.sampleRate, clonedBuffer[i]));
		}
		return pairs;
	}

	synchronized public List<TimeValuePair> getValues() {
		ArrayList<TimeValuePair> values = this.generateTimeValuePairs(this.buffer);
		return values;
	}
}
