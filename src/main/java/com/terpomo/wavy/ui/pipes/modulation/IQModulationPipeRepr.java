package com.terpomo.wavy.ui.pipes.modulation;

import com.terpomo.wavy.pipes.modulation.IQModulationPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IQModulationPipeRepr extends AbstractPipeRepr<IQModulationPipe> {

    private static final String INPUT_SIGNAL = "Input Signal";
    private static final String CARRIER_FREQUENCY = "Carrier Frequency (Hz)";
    private static final String IN_PHASE_OUTPUT = "In-Phase Output";
    private static final String QUADRATURE_OUTPUT = "Quadrature Output";

    @SuppressWarnings({"rawtypes", "unchecked"})
    public IQModulationPipeRepr(IQModulationPipe pipe) {
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

        PipePropertyRepr carrierFreqProperty = new PipePropertyRepr<Float>(Float.class, this, null, CARRIER_FREQUENCY, this.getPipe()::getCarrierFrequency, null, this.getPipe()::setCarrierFrequency);
        pipeProperties.add(carrierFreqProperty);

        PipePropertyRepr inputSignalProperty = new PipePropertyRepr<>(null, this, this.getPipe().getSignalInputPort(), INPUT_SIGNAL, null, null, null);
        pipeProperties.add(inputSignalProperty);

        PipePropertyRepr inPhaseOutputProperty = new PipePropertyRepr<>(null, this, null, IN_PHASE_OUTPUT, null, this.getPipe().getIOutputPort(), null);
        pipeProperties.add(inPhaseOutputProperty);

        PipePropertyRepr quadratureOutputProperty = new PipePropertyRepr<>(null, this, null, QUADRATURE_OUTPUT, null, this.getPipe().getQOutputPort(), null);
        pipeProperties.add(quadratureOutputProperty);

        return pipeProperties;
    }
}
