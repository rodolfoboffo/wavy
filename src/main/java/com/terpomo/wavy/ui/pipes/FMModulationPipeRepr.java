package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.pipes.FMModulationPipe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FMModulationPipeRepr extends AbstractPipeRepr<FMModulationPipe> {

    private static final String INPUT_CHANNEL = "Input";
    private static final String OUTPUT_CHANNEL = "Output";

    @SuppressWarnings({"rawtypes", "unchecked"})
    public FMModulationPipeRepr(FMModulationPipe pipe, String name) {
        super(pipe, name);

        GridBagLayout contentLayout = new GridBagLayout();
        this.getContentPanel().setLayout(contentLayout);

        this.buildCustomPipeControls();
    }

    private void buildCustomPipeControls() {
        List<PipePropertyRepr> pipeProperties = this.createPipePropertiesForInputs();
        this.layoutPipePropertiesOnGrid(pipeProperties);
    }

    @SuppressWarnings("rawtypes")
    private List<PipePropertyRepr> createPipePropertiesForInputs() {
        List<PipePropertyRepr> pipeProperties = new ArrayList<>();

        PipePropertyRepr sampleRateProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, SAMPLE_RATE, this.getPipe()::getSampleRate, null, this.getPipe()::setSampleRate);
        pipeProperties.add(sampleRateProperty);

        PipePropertyRepr inputChannelProperty = new PipePropertyRepr<>(null, this, this.getPipe().getInputPort(), INPUT_CHANNEL, null, null, null);
        pipeProperties.add(inputChannelProperty);

        PipePropertyRepr outputChannelProperty = new PipePropertyRepr<>(null, this, null, OUTPUT_CHANNEL, null, this.getPipe().getOutputPort(), null);
        pipeProperties.add(outputChannelProperty);

        return pipeProperties;
    }
}
