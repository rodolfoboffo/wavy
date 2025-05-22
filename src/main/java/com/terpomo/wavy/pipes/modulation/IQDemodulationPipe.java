package com.terpomo.wavy.pipes.modulation;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.math.MathConstants;
import com.terpomo.wavy.signals.ConstantWave;

public class IQDemodulationPipe extends AbstractPipe {

    private ConstantWave carrier;
    private ConstantWave quadratureCarrier;
    private int sampleRate;
    private float carrierFrequency;
    private final OutputPort outputPort;
    private final InputPort iInputPort;
    private final InputPort qInputPort;

    public IQDemodulationPipe(String pipeName) {
        super(pipeName);
        this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
        this.carrierFrequency = 100000000f;
        this.buildInputPorts(2);
        this.buildOutputPorts(1);
        this.outputPort = this.getOutputPorts().get(0);
        this.iInputPort = this.getInputPorts().get(0);
        this.qInputPort = this.getInputPorts().get(1);
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
                    Float iValue = this.getIInputPort().getBuffer().pickOne();
                    Float qValue = this.getQInputPort().getBuffer().pickOne();
                    float iCarrierValue = this.carrier.getNextValue();
                    float qCarrierValue = this.quadratureCarrier.getNextValue();
                    Float value = iValue * iCarrierValue + qValue * qCarrierValue;
                    this.putThroughPort(this.getOutputPort(), value);
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

    public int getSampleRate() {
        return sampleRate;
    }

    synchronized public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        this.resetCarriersWaves();
    }

    public OutputPort getOutputPort() {
        return outputPort;
    }

    public InputPort getIInputPort() {
        return iInputPort;
    }

    public InputPort getQInputPort() {
        return qInputPort;
    }
}
