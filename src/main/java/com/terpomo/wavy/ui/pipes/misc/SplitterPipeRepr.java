package com.terpomo.wavy.ui.pipes.misc;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.pipes.misc.SplitterPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class SplitterPipeRepr extends AbstractPipeRepr<SplitterPipe> {

    private static final String NUMBER_OF_OUTPUTS = "# of Outputs";
    private static final String INPUT_CHANNEL = "Input";
    private static final String CHANNEL_NUMBER = "Channel %d";
    private final GridBagLayout contentLayout;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public SplitterPipeRepr(SplitterPipe pipe) {
        super(pipe);

        this.contentLayout = new GridBagLayout();
        this.getContentPanel().setLayout(this.contentLayout);

        this.buildCustomPipeControls();
        this.getPipe().addPropertyChangeListener(AbstractPipe.PROPERTY_PIPE_OUTPUT_PORTS, new OutputPortsPropertyChangeListener());
    }

    private void buildCustomPipeControls() {
        List<PipePropertyRepr<?>> pipeProperties = this.createPipePropertiesForInputs();
        this.layoutPipePropertiesOnGrid(pipeProperties);
    }

    @SuppressWarnings("rawtypes")
    private List<PipePropertyRepr<?>> createPipePropertiesForInputs() {
        List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();
        PipePropertyRepr inputChannelProperty = new PipePropertyRepr<>(null, this, this.getPipe().getInputPort(), INPUT_CHANNEL, null, null, null);
        pipeProperties.add(inputChannelProperty);

        PipePropertyRepr numOfChannelsProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, NUMBER_OF_OUTPUTS, this.getPipe()::getNumberOfChannels, null, this.getPipe()::setNumberOfChannels);
        pipeProperties.add(numOfChannelsProperty);

        for (int i = 0; i < this.getPipe().getOutputPorts().size(); i++) {
            OutputPort _pipe = this.getPipe().getOutputPorts().get(i);
            PipePropertyRepr signalOutputProperty = new PipePropertyRepr<>(null, this, null, String.format(CHANNEL_NUMBER, i+1), null, _pipe);
            pipeProperties.add(signalOutputProperty);
        }
        return pipeProperties;
    }

    class OutputPortsPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SplitterPipeRepr.this.buildCustomPipeControls();
                    SplitterPipeRepr.this.revalidate();
                    SplitterPipeRepr.this.repaint();
                }
            });
        }
    }
}
