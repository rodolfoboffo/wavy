package com.terpomo.wavy.pipes.modulation;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.math.ArcSineTable;
import com.terpomo.wavy.math.MathConstants;
import com.terpomo.wavy.math.SineTable;

public class FMDemodulationPipe extends AbstractPipe {

    private float amplitude;
    private int sampleRate;
    private float rad;
    private float instantaneousFrequency;
    private final InputPort inputPort;
    private final OutputPort outputPort;
    private float modulationIndex = 75000f;
    private final ArcSineTable arcSineTable;

    public FMDemodulationPipe(Float rad) {
        this.arcSineTable = ArcSineTable.DEFAULT_ARC_SINE_TABLE;
        this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
        this.rad = rad;
        this.instantaneousFrequency = 0;
        this.amplitude = 1.0f;
        this.buildInputPorts(1);
        this.buildOutputPorts(1);
        this.inputPort = this.getInputPorts().get(0);
        this.outputPort = this.getOutputPorts().get(0);
    }

    public FMDemodulationPipe() {
        this(0f);
    }

    @Override
    synchronized protected void doWork() {
        if (this.allPortsConnected()) {
            if (!this.getOutputPort().getLinkedPort().getBuffer().isFull() && !this.getInputPort().getBuffer().isEmpty()) {
                float sin = this.getInputPort().getBuffer().pickOne();
                float newRad1 = (this.arcSineTable.getValue(sin) + MathConstants.PI2) % MathConstants.PI2;
                float newRad2 = newRad1 <= MathConstants.PI ? MathConstants.PI - newRad1 : MathConstants.PI2 - (newRad1 - MathConstants.PI);
                float predictedNewRad = (1.0f / this.sampleRate * MathConstants.PI2 * this.instantaneousFrequency + this.rad) % MathConstants.PI2;
                float diff1 = (predictedNewRad - newRad1 + MathConstants.PI2) % MathConstants.PI2;
                float diff2 = (predictedNewRad - newRad2 + MathConstants.PI2) % MathConstants.PI2;
                diff1 = diff1 < MathConstants.PI ? diff1 : MathConstants.PI - (diff1 - MathConstants.PI);
                diff2 = diff2 < MathConstants.PI ? diff2 : MathConstants.PI - (diff2 - MathConstants.PI);
                float minorDiff = diff1 <= diff2 ? newRad1 : newRad2;
                this.instantaneousFrequency =  minorDiff * this.sampleRate / MathConstants.PI2;
                float modulatedValue = (this.modulationIndex - this.instantaneousFrequency);
                this.putThroughPort(this.outputPort, modulatedValue);
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
