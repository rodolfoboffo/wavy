package com.terpomo.wavy.pipes.modulation;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.math.MathConstants;
import com.terpomo.wavy.math.SineTable;

public class FMModulationPipe extends AbstractPipe {

    private float amplitude;
    private int sampleRate;
    private float phase;
    private final InputPort inputPort;
    private final OutputPort outputPort;
    private float modulationIndex = 75000f;
    private final SineTable sineTable;

    public FMModulationPipe(Float phase) {
        this.sineTable = SineTable.DEFAULT_SINE_TABLE;
        this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
        this.phase = phase;
        this.amplitude = 1.0f;
        this.buildInputPorts(1);
        this.buildOutputPorts(1);
        this.inputPort = this.getInputPorts().get(0);
        this.outputPort = this.getOutputPorts().get(0);
    }

    public FMModulationPipe() {
        this(0f);
    }

    @Override
    synchronized protected void doWork() {
        if (this.allPortsConnected()) {
            if (!this.getOutputPort().getLinkedPort().getBuffer().isFull() && !this.getInputPort().getBuffer().isEmpty()) {
                float signal = this.getInputPort().getBuffer().pickOne();
                this.phase = ((1.0f / this.sampleRate * MathConstants.PI2 * this.modulationIndex * signal + this.phase) + MathConstants.PI2) % MathConstants.PI2;
                float value = this.amplitude * this.sineTable.getValue(this.phase);
                this.putThroughPort(this.outputPort, value);
            }
        }
    }

    public InputPort getInputPort() {
        return inputPort;
    }

    public OutputPort getOutputPort() {
        return outputPort;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    synchronized public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public Float getModulationIndex() {
        return modulationIndex;
    }

    synchronized public void setModulationIndex(Float modulationIndex) {
        this.modulationIndex = modulationIndex;
    }

    public float getAmplitude() {
        return amplitude;
    }

    synchronized public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }
}
