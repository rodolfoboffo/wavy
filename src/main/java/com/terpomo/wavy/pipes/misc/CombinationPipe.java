package com.terpomo.wavy.pipes.misc;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.flow.SignalBuffer;
import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;
import com.terpomo.wavy.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class CombinationPipe extends AbstractPipe {

    private static final int DEFAULT_NUMBER_OF_CHANNELS = 2;
    private int numberOfChannels;
    private List<Float> scaleFactors;
    private Float dcShift;
    private Float outputScale;

    public CombinationPipe() {
        super();
        this.numberOfChannels = DEFAULT_NUMBER_OF_CHANNELS;
        this.dcShift = 0f;
        this.outputScale = 1f;
        this.scaleFactors = new ArrayList<>();
        this.buildPortsAndFactors();
    }

    public OutputPort getOutputPort() {
        return this.getOutputPorts().get(0);
    }

    @Override
    synchronized protected void doWork() {
        if (this.getOutputPort().getLinkedPort() != null) {
            SignalBuffer buffer = this.getOutputPort().getLinkedPort().getBuffer();
            for (InputPort input : this.getInputPorts()) {
                if (input.getLinkedPort() == null || input.getBuffer().isEmpty())
                    return;
            }
            if (!buffer.isFull()) {
                float value = 0.0f;
                for (int i = 0; i < this.getInputPorts().size(); i++) {
                    value += this.getInputPorts().get(i).getBuffer().pickOne() * this.getScaleFactorForChannel(i);
                }
                value = (value * this.outputScale) + this.dcShift;
                this.putThroughPort(this.getOutputPort(), value);
            }
        }
    }

    synchronized private void buildPortsAndFactors() {
        this.dispose();
        try {
            this.buildOutputPorts(1);
            this.buildInputPorts(this.numberOfChannels);
            List<Float> scaleFactors = ListUtils.buildNewList(this.numberOfChannels, Float.class, this.getScaleFactors(), Float.class.getDeclaredConstructor(float.class), new Object[]{1.0f});
            this.setScaleFactors(scaleFactors);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_NUM_CHANNELS)
    synchronized public void setNumberOfChannels(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
        this.buildPortsAndFactors();
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_NUM_CHANNELS)
    public int getNumberOfChannels() {
        return numberOfChannels;
    }

    public List<Float> getScaleFactors() {
        return scaleFactors;
    }

    synchronized public void setScaleFactors(List<Float> scaleFactors) {
        this.scaleFactors = scaleFactors;
    }

    public Float getScaleFactorForChannel(int channelIndex) {
        return this.getScaleFactors().get(channelIndex);
    }

    public void setScaleFactorForChannel(int channelIndex, float factor) {
        this.getScaleFactors().set(channelIndex, factor);
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_DC_SHIFT)
    public Float getDcShift() {
        return dcShift;
    }

    @MarshalAttr(attrName= MarshallingKeys.KEY_DC_SHIFT)
    synchronized public void setDcShift(Float dcShift) {
        this.dcShift = dcShift;
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
