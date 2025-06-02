package com.terpomo.wavy.pipes.modulation;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.math.MathConstants;
import com.terpomo.wavy.signals.ConstantWave;

public class IQModulationPipe extends AbstractPipe {

    private ConstantWave carrier;
    private ConstantWave quadratureCarrier;
    private int sampleRate;
    private float carrierFrequency;
    private final InputPort signalInputPort;
    private final OutputPort iOutputPort;
    private final OutputPort qOutputPort;

    public IQModulationPipe() {
        super();
        this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
        this.carrierFrequency = 100000000f;
        this.buildInputPorts(1);
        this.buildOutputPorts(2);
        this.signalInputPort = this.getInputPorts().get(0);
        this.iOutputPort = this.getOutputPorts().get(0);
        this.qOutputPort = this.getOutputPorts().get(1);
        this.resetCarriersWaves();
    }

    @Override
    synchronized protected void doWork() {
        if (this.allPortsConnected()) {
            int minInputBufferSize = this.getMinInputBufferSizes();
            int minOutputCapacity = this.getMinOutputBufferRemainingCapacity();
            if (minInputBufferSize > 0 && minOutputCapacity > 0) {
                int numOfSamples = Math.min(minInputBufferSize, minOutputCapacity);
                for (int i = 0; i < numOfSamples; i++) {
                    Float inputSignal = this.getSignalInputPort().getBuffer().pickOne();
                    float iValue = this.carrier.getNextValue();
                    float qValue = this.quadratureCarrier.getNextValue();
                    this.putThroughPort(this.getIOutputPort(), iValue*inputSignal);
                    this.putThroughPort(this.getQOutputPort(), qValue*inputSignal);
                }
            }
        }
    }

    public float getCarrierFrequency() {
        return carrierFrequency;
    }

    synchronized public void setCarrierFrequency(float carrierFrequency) {
        this.carrierFrequency = carrierFrequency;
        this.resetCarriersWaves();
    }

    private void resetCarriersWaves() {
        this.carrier = new ConstantWave(this.sampleRate, this.carrierFrequency);
        this.quadratureCarrier = new ConstantWave(this.sampleRate, this.carrierFrequency, MathConstants.HALF_PI);
    }

    public InputPort getSignalInputPort() {
        return signalInputPort;
    }

    public OutputPort getQOutputPort() {
        return qOutputPort;
    }

    public OutputPort getIOutputPort() {
        return iOutputPort;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    synchronized public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        this.resetCarriersWaves();
    }
}
