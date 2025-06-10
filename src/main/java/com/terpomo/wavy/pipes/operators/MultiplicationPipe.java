package com.terpomo.wavy.pipes.operators;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.flow.SignalBuffer;
import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;

public class MultiplicationPipe extends AbstractPipe {

    private static final int DEFAULT_NUMBER_OF_CHANNELS = 2;
    private int numberOfChannels;
    private Float outputScale;

    public MultiplicationPipe() {
        super();
        this.numberOfChannels = DEFAULT_NUMBER_OF_CHANNELS;
        this.outputScale = 1f;
        this.buildPorts();
    }

    public OutputPort getOutputPort() {
        return this.getOutputPorts().get(0);
    }

    @Override
    synchronized protected void doWork() {
        if (this.getOutputPort().getLinkedPort() != null) {
            SignalBuffer buffer = this.getOutputPort().getLinkedPort().getBuffer();
            for (InputPort input : this.getInputPorts()) {
                if (input.getLinkedPort() != null && input.getBuffer().isEmpty())
                    return;
            }
            if (!buffer.isFull()) {
                float value = 1.0f;
                for (int i = 0; i < this.getInputPorts().size(); i++) {
                    if (this.getInputPorts().get(i).getLinkedPort() != null)
                        value *= this.getInputPorts().get(i).getBuffer().pickOne();
                }
                value = (value * this.outputScale);
                this.putThroughPort(this.getOutputPort(), value);
            }
        }
    }

    synchronized private void buildPorts() {
        this.dispose();
        this.buildOutputPorts(1);
        this.buildInputPorts(this.numberOfChannels);
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_NUM_CHANNELS)
    synchronized public void setNumberOfChannels(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
        this.buildPorts();
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_NUM_CHANNELS)
    public int getNumberOfChannels() {
        return numberOfChannels;
    }
    @MarshalAttr(attrName= MarshallingKeys.KEY_OUTPUT_SCALE)
    public Float getOutputScale() {
        return outputScale;
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_OUTPUT_SCALE)
    synchronized public void setOutputScale(Float outputScale) {
        this.outputScale = outputScale;
    }
}
