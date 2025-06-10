package com.terpomo.wavy.ui.pipes.modulation;

import com.terpomo.wavy.pipes.modulation.FMModulationPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FMModulationPipeRepr extends AbstractPipeRepr<FMModulationPipe> {

    private static final String INPUT_CHANNEL = "Input";
    private static final String OUTPUT_CHANNEL = "Output";
    private static final String MODULATION_INDEX = "Modulation Index (Hz)";
    private static final String AMPLITUDE = "Amplitude";

    @SuppressWarnings({"rawtypes", "unchecked"})
    public FMModulationPipeRepr(FMModulationPipe pipe) {
        super(pipe);

        GridBagLayout contentLayout = new GridBagLayout();
        this.getContentPanel().setLayout(contentLayout);

        this.buildCustomPipeControls();
    }

    private void buildCustomPipeControls() {
        List<PipePropertyRepr<?>> pipeProperties = this.createPipeProperties();
        this.layoutPipePropertiesOnGrid(pipeProperties);
    }

    @SuppressWarnings("rawtypes")
    private List<PipePropertyRepr<?>> createPipeProperties() {
        List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();

        PipePropertyRepr sampleRateProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, SAMPLE_RATE, this.getPipe()::getSampleRate, null, this.getPipe()::setSampleRate);
        pipeProperties.add(sampleRateProperty);

        PipePropertyRepr modulationFreqProp = new PipePropertyRepr<Float>(Float.class, this, null, MODULATION_INDEX, this.getPipe()::getModulationIndex, null, this.getPipe()::setModulationIndex);
        pipeProperties.add(modulationFreqProp);

        PipePropertyRepr amplitudeProp = new PipePropertyRepr<Float>(Float.class, this, null, AMPLITUDE, this.getPipe()::getAmplitude, null, this.getPipe()::setAmplitude);
        pipeProperties.add(amplitudeProp);

        PipePropertyRepr inputChannelProperty = new PipePropertyRepr<>(null, this, this.getPipe().getInputPort(), INPUT_CHANNEL, null, null, null);
        pipeProperties.add(inputChannelProperty);

        PipePropertyRepr outputChannelProperty = new PipePropertyRepr<>(null, this, null, OUTPUT_CHANNEL, null, this.getPipe().getOutputPort(), null);
        pipeProperties.add(outputChannelProperty);

        return pipeProperties;
    }
}
