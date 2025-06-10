package com.terpomo.wavy.ui.pipes.misc;

import com.terpomo.wavy.pipes.misc.ResamplingPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ResamplingPipeRepr extends AbstractPipeRepr<ResamplingPipe> {
    
    private static final String INPUT_SAMPLE_RATE = "Input Sample Rate";
    private static final String OUTPUT_SAMPLE_RATE = "Output Sample Rate";
    private static final String INTERPOLATION_METHOD = "Interpolation Method";

    public ResamplingPipeRepr(ResamplingPipe pipe) {
        super(pipe);

        GridBagLayout contentLayout = new GridBagLayout();
        this.getContentPanel().setLayout(contentLayout);

        this.buildCustomPipeControls();
    }

    private void buildCustomPipeControls() {
        List<PipePropertyRepr<?>> pipeProperties = this.createPipeProperties();
        this.layoutPipePropertiesOnGrid(pipeProperties);
    }

    private List<PipePropertyRepr<?>> createPipeProperties() {
        List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();
        PipePropertyRepr<Integer> inputChannelProperty = new PipePropertyRepr<>(Integer.class, this, this.getPipe().getInputPort(), INPUT_SAMPLE_RATE, this.getPipe()::getInputSampleRate, null, this.getPipe()::setInputSampleRate);
        pipeProperties.add(inputChannelProperty);

        PipePropertyRepr<Integer> outputChannelProperty = new PipePropertyRepr<>(Integer.class, this, null, OUTPUT_SAMPLE_RATE, this.getPipe()::getOutputSampleRate, this.getPipe().getOutputPort(), this.getPipe()::setOutputSampleRate);
        pipeProperties.add(outputChannelProperty);

        return pipeProperties;
    }
}
