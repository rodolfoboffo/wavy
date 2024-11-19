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

	private final InputPort inputPort;
	private final Buffer buffer;
	private final int sampleRate;
	private final Object lock;
	private LocalDateTime timestamp;
	
	public OscilloscopePipe() {
		this.inputPort = new InputPort(this);
		this.lock = new Object();
		this.inputPorts.add(this.inputPort);
		this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
		this.buffer = new Buffer(Constants.DEFAULT_SAMPLE_RATE);
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
			if (this.buffer.getRemainingCapacity() < samplesToFetch) {
				this.buffer.fetch(samplesToFetch - this.buffer.getRemainingCapacity());
			}
			this.buffer.putAll(this.inputPort.getBuffer().fetch(samplesToFetch));
		}
	}

	private ArrayList<TimeValuePair> generateTimeValuePairs(Buffer buffer) {
		ArrayList<TimeValuePair> pairs = new ArrayList<>();
		List<Float> clonedBuffer = buffer.getAll();
		for (int i = 0; i < clonedBuffer.size(); i++) {
			pairs.add(new TimeValuePair((float)i/this.sampleRate, clonedBuffer.get(i)));
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
