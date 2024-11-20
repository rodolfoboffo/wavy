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

	private static final float MAX_SCALE = 3.0f;
	private static final float DEFAULT_SCALE = 1.0f;
	private final InputPort inputPort;
	private final Buffer buffer;
	private final int sampleRate;
	private final Object lock;
	private LocalDateTime timestamp;
	private float scale;
	
	public OscilloscopePipe(float scale) {
		this.lock = new Object();
		this.inputPort = new InputPort(this);
		this.inputPorts.add(this.inputPort);
		this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
		this.scale = scale;
		this.buffer = new Buffer(Constants.DEFAULT_SAMPLE_RATE, true);
	}

	public OscilloscopePipe() {
		this(DEFAULT_SCALE);
	}

	@Override
	public void initialize() {
		this.timestamp = LocalDateTime.now();
		super.initialize();
	}

	@Override
	protected void doWork() {
		synchronized (this.lock) {
			LocalDateTime now = LocalDateTime.now();
			long intervalMillis = ChronoUnit.MILLIS.between(this.timestamp, now);
			this.timestamp = now;
			int samplesToFetch = (int)(1.0f / 1000 * intervalMillis * this.sampleRate);
			this.buffer.putAll(this.inputPort.getBuffer().fetch(samplesToFetch));
		}
	}

	private ArrayList<TimeValuePair> generateTimeValuePairs(Buffer buffer) {
		ArrayList<TimeValuePair> pairs = new ArrayList<>();
		Float[] clonedBuffer = buffer.getAll();
		for (int i = 0; i < clonedBuffer.length; i++) {
			pairs.add(new TimeValuePair((float)i/this.sampleRate, clonedBuffer[i]));
		}
		return pairs;
	}

	public List<TimeValuePair> getValues() {
		synchronized (this.lock) {
			ArrayList<TimeValuePair> values = this.generateTimeValuePairs(this.buffer);
			return values;
		}
	}
}
