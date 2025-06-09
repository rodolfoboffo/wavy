package com.terpomo.wavy.ui.pipes.operators;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.pipes.operators.MultiplicationPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MultiplicationPipeRepr extends AbstractPipeRepr<MultiplicationPipe> {
    private static final String OUTPUT_CHANNEL = "Output Channel";
    private static final String CHANNEL_NUMBER = "Channel #%d";
    private static final String OUTPUT_SCALE_FACTOR = "Output Scale Factor";
    private static final String NUMBER_OF_CHANNELS = "Number of Channels";
    private final GridBagLayout contentLayout;

    public MultiplicationPipeRepr(MultiplicationPipe pipe) {
        super(pipe);
        
        this.contentLayout = new GridBagLayout();
        this.getContentPanel().setLayout(this.contentLayout);

        this.buildCustomPipeControls();
        this.getPipe().addPropertyChangeListener(AbstractPipe.PROPERTY_PIPE_INPUT_PORTS, new InputPortsPropertyChangeListener());
    }

    private void buildCustomPipeControls() {
        List<PipePropertyRepr<?>> pipeProperties = this.createPipePropertiesForInputs();
        this.layoutPipePropertiesOnGrid(pipeProperties);
    }

    @SuppressWarnings("rawtypes")
    private List<PipePropertyRepr<?>> createPipePropertiesForInputs() {
        List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();
        PipePropertyRepr outputChannelProperty = new PipePropertyRepr<>(null, this, null, OUTPUT_CHANNEL, null, this.getPipe().getOutputPort(), null);
        pipeProperties.add(outputChannelProperty);

        PipePropertyRepr<Float> outputScaleFactorProperty = new PipePropertyRepr<Float>(Float.class, this, null, OUTPUT_SCALE_FACTOR, this.getPipe()::getOutputScale, null, this.getPipe()::setOutputScale);
        pipeProperties.add(outputScaleFactorProperty);

        PipePropertyRepr numOfChannelsProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, NUMBER_OF_CHANNELS, this.getPipe()::getNumberOfChannels, null, this.getPipe()::setNumberOfChannels);
        pipeProperties.add(numOfChannelsProperty);

        for (int i = 0; i < this.getPipe().getInputPorts().size(); i++) {
            InputPort _port = this.getPipe().getInputPorts().get(i);
            PipePropertyRepr<?> inputScalingProperty = new PipePropertyRepr<>(null, this, _port, String.format(CHANNEL_NUMBER, i + 1), null, null, null);
            pipeProperties.add(inputScalingProperty);
        }
        return pipeProperties;
    }

    class InputPortsPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MultiplicationPipeRepr.this.buildCustomPipeControls();
                    MultiplicationPipeRepr.this.revalidate();
                    MultiplicationPipeRepr.this.repaint();
                }
            });
        }
    }
}
