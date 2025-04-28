package com.terpomo.wavy.ui.pipes.modulation;

import com.terpomo.wavy.pipes.modulation.IQDemodulationPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IQDemodulationPipeRepr extends AbstractPipeRepr<IQDemodulationPipe> {

    private static final String OUTPUT_SIGNAL = "Output Signal";
    private static final String CARRIER_FREQUENCY = "Carrier Frequency (Hz)";
    private static final String IN_PHASE_INPUT = "In-Phase Input";
    private static final String QUADRATURE_INPUT = "Quadrature Input";

    @SuppressWarnings({"rawtypes", "unchecked"})
    public IQDemodulationPipeRepr(IQDemodulationPipe pipe, String name) {
        super(pipe, name);

        GridBagLayout contentLayout = new GridBagLayout();
        this.getContentPanel().setLayout(contentLayout);

        this.buildCustomPipeControls();
    }

    private void buildCustomPipeControls() {
        List<PipePropertyRepr> pipeProperties = this.createPipeProperties();
        this.layoutPipePropertiesOnGrid(pipeProperties);
    }

    @SuppressWarnings("rawtypes")
    private List<PipePropertyRepr> createPipeProperties() {
        List<PipePropertyRepr> pipeProperties = new ArrayList<>();

        PipePropertyRepr sampleRateProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, SAMPLE_RATE, this.getPipe()::getSampleRate, null, this.getPipe()::setSampleRate);
        pipeProperties.add(sampleRateProperty);

        PipePropertyRepr carrierFreqProperty = new PipePropertyRepr<Float>(Float.class, this, null, CARRIER_FREQUENCY, this.getPipe()::getCarrierFrequency, null, this.getPipe()::setCarrierFrequency);
        pipeProperties.add(carrierFreqProperty);

        PipePropertyRepr inPhaseSignalProperty = new PipePropertyRepr<>(null, this, this.getPipe().getIInputPort(), IN_PHASE_INPUT, null, null, null);
        pipeProperties.add(inPhaseSignalProperty);

        PipePropertyRepr quadratureSignalProperty = new PipePropertyRepr<>(null, this, this.getPipe().getQInputPort(), QUADRATURE_INPUT, null, null, null);
        pipeProperties.add(quadratureSignalProperty);

        PipePropertyRepr outputProperty = new PipePropertyRepr<>(null, this, null, OUTPUT_SIGNAL, null, this.getPipe().getOutputPort(), null);
        pipeProperties.add(outputProperty);

        return pipeProperties;
    }
}
