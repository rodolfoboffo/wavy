package com.terpomo.wavy.pipes.misc;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;
import com.terpomo.wavy.marshal.PipeMarshaller;

public class SplitterPipe extends AbstractPipe {

    public static final int DEFAULT_NUMBER_OF_CHANNELS = 2;
    private int numberOfChannels;
    private final InputPort inputPort;

    public SplitterPipe(int numberOfChannels) {
        super();
        this.numberOfChannels = numberOfChannels;
        this.inputPort = new InputPort(this);
        this.getInputPorts().add(this.inputPort);
        this.buildPorts();
    }

    private void buildPorts() {
        this.buildOutputPorts(this.numberOfChannels);
        this.firePropertyChange(PROPERTY_PIPE_OUTPUT_PORTS, null, this.getOutputPorts());
    }

    @MarshalAttr(attrName = MarshallingKeys.KEY_NUM_CHANNELS)
    public void setNumberOfChannels(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
        this.buildPorts();
    }

    public SplitterPipe() {
        this(DEFAULT_NUMBER_OF_CHANNELS);
    }

    public InputPort getInputPort() {
        return inputPort;
    }

    @MarshalAttr(attrName = MarshallingKeys.KEY_NUM_CHANNELS)
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
                    this.putAllThroughPort(port, values);
            }
        }
    }
}
