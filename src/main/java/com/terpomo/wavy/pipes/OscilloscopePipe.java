package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.Buffer;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.oscilloscope.TimeValuePair;

import java.util.ArrayList;
import java.util.List;

public class OscilloscopePipe extends AbstractPipe {

	private static final int DEFAULT_OSCILLOSCOPE_BUFFER_SIZE = 102400;
	private final InputPort inputPort;
	private final Buffer buffer;
	private final int sampleRate;
	private final float timeFrameInSec;
	private final int timeFrameInSamples;
	private final Object lock;
	private List<TimeValuePair> values;
	private boolean isDataReady;
	
	public OscilloscopePipe() {
		this.inputPort = new InputPort(this);
		this.lock = new Object();
		this.inputPorts.add(this.inputPort);
		this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
		this.timeFrameInSec = 0.1f;
		this.timeFrameInSamples = (int) (this.sampleRate * this.timeFrameInSec);
		this.buffer = new Buffer(this.timeFrameInSamples);
		this.values = new ArrayList<>(this.timeFrameInSamples);
		this.isDataReady = false;
	}
	
	@Override
	protected void doWork() {
		if (this.buffer.getCapacity() >= this.timeFrameInSamples) {
			synchronized (this.lock) {
				if (this.buffer.getRemainingCapacity() < this.inputPort.getBuffer().getSize()) {
					this.buffer.fetch(this.inputPort.getBuffer().getSize() - this.buffer.getRemainingCapacity());
				}
				this.buffer.putAll(this.inputPort.getBuffer().fetchAll());
				this.values = this.generateTimeValuePairs(this.buffer);
			}
		}
	}

	private ArrayList<TimeValuePair> generateTimeValuePairs(Buffer buffer) {
		ArrayList<TimeValuePair> pairs = new ArrayList<TimeValuePair>();
		List<Float> clonedBuffer = buffer.getAll();
		for (int i = 0; i < clonedBuffer.size(); i++) {
			pairs.add(new TimeValuePair((float)i/this.sampleRate, clonedBuffer.get(i)));
		}
		return pairs;
	}

	public List<TimeValuePair> getValues() {
		synchronized (this.lock) {
			this.isDataReady = false;
			return values;
		}
	}

	public boolean isDataReady() {
		return isDataReady;
	}
}
