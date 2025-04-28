package com.terpomo.wavy.ui.pipes.misc;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.pipes.misc.CombinationPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CombinationPipeRepr extends AbstractPipeRepr<CombinationPipe> {
    private static final String CHANNEL_NUMBER_SCALE_FACTOR = "Channel #%d scale factor";
    private static final String OUTPUT_CHANNEL = "Output Channel";
    private static final String DC_SHIFT = "DC Shift";
    private static final String OUTPUT_SCALE_FACTOR = "Output Scale Factor";
    private static final String NUMBER_OF_CHANNELS = "Number of Channels";
    private final GridBagLayout contentLayout;

    public CombinationPipeRepr(CombinationPipe pipe, String name) {
        super(pipe, name);
        
        this.contentLayout = new GridBagLayout();
        this.getContentPanel().setLayout(this.contentLayout);

        this.buildCustomPipeControls();
        this.getPipe().addPropertyChangeListener(AbstractPipe.PROPERTY_PIPE_INPUT_PORTS, new InputPortsPropertyChangeListener());
    }

    private void buildCustomPipeControls() {
        java.util.List<PipePropertyRepr> pipeProperties = this.createPipePropertiesForInputs();
        this.layoutPipePropertiesOnGrid(pipeProperties);
    }

    @SuppressWarnings("rawtypes")
    private java.util.List<PipePropertyRepr> createPipePropertiesForInputs() {
        List<PipePropertyRepr> pipeProperties = new ArrayList<>();
        PipePropertyRepr outputChannelProperty = new PipePropertyRepr<>(null, this, null, OUTPUT_CHANNEL, null, this.getPipe().getOutputPort(), null);
        pipeProperties.add(outputChannelProperty);

        PipePropertyRepr<Float> outputScaleFactorProperty = new PipePropertyRepr<Float>(Float.class, this, null, OUTPUT_SCALE_FACTOR, this.getPipe()::getOutputScale, null, this.getPipe()::setOutputScale);
        pipeProperties.add(outputScaleFactorProperty);

        PipePropertyRepr<Float> dcShiftProperty = new PipePropertyRepr<Float>(Float.class, this, null, DC_SHIFT, this.getPipe()::getDcShift, null, this.getPipe()::setDcShift);
        pipeProperties.add(dcShiftProperty);

        PipePropertyRepr numOfChannelsProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, NUMBER_OF_CHANNELS, this.getPipe()::getNumberOfChannels, null, this.getPipe()::setNumberOfChannels);
        pipeProperties.add(numOfChannelsProperty);

        for (int i = 0; i < this.getPipe().getInputPorts().size(); i++) {
            final int channelIndex = i;
            InputPort _port = this.getPipe().getInputPorts().get(i);
            PipePropertyRepr<Float> inputScalingProperty = new PipePropertyRepr<Float>(Float.class, this, _port, String.format(CHANNEL_NUMBER_SCALE_FACTOR, channelIndex + 1), new Supplier<Float>() {
                @Override
                public Float get() {
                    return CombinationPipeRepr.this.getPipe().getScaleFactorForChannel(channelIndex);
                }
            }, null, new Consumer<Float>() {
                @Override
                public void accept(Float aFloat) {
                    CombinationPipeRepr.this.getPipe().setScaleFactorForChannel(channelIndex, aFloat);
                }
            });
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
                    CombinationPipeRepr.this.buildCustomPipeControls();
                    CombinationPipeRepr.this.revalidate();
                    CombinationPipeRepr.this.repaint();
                }
            });
        }
    }
}
