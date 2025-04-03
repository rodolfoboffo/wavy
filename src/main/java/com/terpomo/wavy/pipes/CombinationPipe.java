package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.Buffer;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class CombinationPipe extends AbstractPipe {

    private static final int DEFAULT_NUMBER_OF_CHANNELS = 2;
    private int numberOfChannels;
    private List<Float> scaleFactors;

    public CombinationPipe() {
        this.numberOfChannels = DEFAULT_NUMBER_OF_CHANNELS;
        this.scaleFactors = new ArrayList<>();
        this.buildPortsAndFactors();
    }

    public OutputPort getOutputPort() {
        return this.getOutputPorts().get(0);
    }

    @Override
    synchronized protected void doWork() {
        if (this.getOutputPort().getLinkedPort() != null) {
            Buffer buffer = this.getOutputPort().getLinkedPort().getBuffer();
            for (InputPort input : this.getInputPorts()) {
                if (input.getLinkedPort() == null || input.getBuffer().isEmpty())
                    return;
            }
            synchronized (buffer) {
                if (!buffer.isFull()) {
                    float value = 0.0f;
                    for (int i = 0; i < this.getInputPorts().size(); i++) {
                        value += this.getInputPorts().get(i).getBuffer().pickOne() * this.getScaleFactorForChannel(i);
                    }
                    buffer.put(value);
                }
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

    synchronized public void setNumberOfChannels(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
        this.buildPortsAndFactors();
    }

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
}
