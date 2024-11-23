package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.pipes.SplitterPipe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SplitterPipeRepr extends AbstractPipeRepr {

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

        List<PipePropertyRepr> pipeProperties = new ArrayList<>();
        PipePropertyRepr inputChannelProperty = new PipePropertyRepr(null, this, this.splitter.getInputPort(), INPUT_CHANNEL, null, null, null);
        pipeProperties.add(inputChannelProperty);

        for (int i = 0; i < this.splitter.getOutputPorts().size(); i++) {
            OutputPort _pipe = this.splitter.getOutputPorts().get(i);
            PipePropertyRepr signalOutputProperty = new PipePropertyRepr(null, this, null, String.format(CHANNEL_NUMBER, i+1), null, _pipe);
            pipeProperties.add(signalOutputProperty);
        }

        this.layoutPipePropertiesOnGrid(pipeProperties);
    }
}
