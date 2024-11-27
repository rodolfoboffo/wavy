package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.pipes.SplitterPipe;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class SplitterPipeRepr extends AbstractPipeRepr {

    private static final String NUMBER_OF_OUTPUTS = "# of Outputs";
    private static final String INPUT_CHANNEL = "Input";
    private static final String CHANNEL_NUMBER = "Channel %d";
    private final GridBagLayout contentLayout;
    private final SplitterPipe splitter;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public SplitterPipeRepr(SplitterPipe pipe, String name) {
        super(pipe, name);
        this.splitter = pipe;

        this.contentLayout = new GridBagLayout();
        this.getContentPanel().setLayout(this.contentLayout);

        this.buildCustomPipeControls();
        this.getPipe().addPropertyChangeListener(AbstractPipe.PROPERTY_PIPE_OUTPUT_PORTS, new OutputPortsPropertyChangeListener());
    }

    private void buildCustomPipeControls() {
        List<PipePropertyRepr> pipeProperties = this.createPipePropertiesForInputs();
        this.layoutPipePropertiesOnGrid(pipeProperties);
    }

    @SuppressWarnings("rawtypes")
    private List<PipePropertyRepr> createPipePropertiesForInputs() {
        List<PipePropertyRepr> pipeProperties = new ArrayList<>();
        PipePropertyRepr inputChannelProperty = new PipePropertyRepr<>(null, this, this.splitter.getInputPort(), INPUT_CHANNEL, null, null, null);
        pipeProperties.add(inputChannelProperty);

        PipePropertyRepr numOfChannelsProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, NUMBER_OF_OUTPUTS, this.splitter.getNumberOfChannels(), null, this.splitter::setNumberOfChannels);
        pipeProperties.add(numOfChannelsProperty);

        for (int i = 0; i < this.splitter.getOutputPorts().size(); i++) {
            OutputPort _pipe = this.splitter.getOutputPorts().get(i);
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
