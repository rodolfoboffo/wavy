package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;

import java.util.ArrayList;

public class SplitterPipe extends AbstractPipe {

    private static final int DEFAULT_NUMBER_OF_CHANNELS = 2;
    private int numberOfChannels;
    private InputPort inputPort;

    public SplitterPipe(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
        this.inputPort = new InputPort(this);
        this.getInputPorts().add(this.inputPort);
        ArrayList<OutputPort> outputChannels = new ArrayList<OutputPort>();
        for (int i = 0; i < this.numberOfChannels; i++) {
            outputChannels.add(new OutputPort(this));
        }
        this.setOutputPorts(outputChannels);
    }

    public SplitterPipe() {
        this(DEFAULT_NUMBER_OF_CHANNELS);
    }

    public InputPort getInputPort() {
        return inputPort;
    }

    public int getNumberOfChannels() {
        return numberOfChannels;
    }

    @Override
    protected void doWork() {
        int minBufferRemaningSize = Integer.MAX_VALUE;
        if (this.getInputPort().getBuffer().getSize() > 0) {
            for (OutputPort port : this.getOutputPorts()) {
                IPort linkedPort = port.getLinkedPort();
                if (linkedPort != null)
                    minBufferRemaningSize = Math.min(linkedPort.getBuffer().getRemainingCapacity(), minBufferRemaningSize);
            }
            Float[] values = this.getInputPort().getBuffer().fetch(minBufferRemaningSize);
            for (OutputPort port : this.getOutputPorts()) {
                IPort linkedPort = port.getLinkedPort();
                if (linkedPort != null)
                    linkedPort.getBuffer().putAll(values);
            }
        }
    }
}
