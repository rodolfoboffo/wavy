package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.pipes.BandPassFilterPipe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BandPassFilterPipeRepr extends AbstractPipeRepr<BandPassFilterPipe> {

    private static final String LOW_FREQUENCY = "Low Frequency";
    private static final String HIGH_FREQUENCY = "High Frequency";
    private static final String RESOLUTION = "Resolution";
    private JPanel pipePropertiesPanel;

    public BandPassFilterPipeRepr(BandPassFilterPipe pipe, String name) {
        super(pipe, name);
        this.createPanels();
        this.buildCustomPipeControls();
    }

    private void createPanels() {
        GridBagLayout contentLayout = new GridBagLayout();
        JPanel contentPanel = getContentPanel();
        contentPanel.setLayout(contentLayout);

        GridBagConstraints constraints = new GridBagConstraints();

        GridBagLayout pipePropertiesLayout = new GridBagLayout();
        this.pipePropertiesPanel = new JPanel();
        this.pipePropertiesPanel.setLayout(pipePropertiesLayout);
        constraints.gridx = 0;
        constraints.gridy = 0;
        contentPanel.add(this.pipePropertiesPanel, constraints);
    }

    @SuppressWarnings("rawtypes")
    private void buildCustomPipeControls() {
        List<PipePropertyRepr> propertyControls = this.createPipePropertiesForInputs();
        this.layoutPipePropertiesOnGrid(this.pipePropertiesPanel, propertyControls);
    }

    @SuppressWarnings("rawtypes")
    protected List<PipePropertyRepr> createPipePropertiesForInputs() {
        List<PipePropertyRepr> pipeProperties = new ArrayList<>();

        PipePropertyRepr sampleRateProp = new PipePropertyRepr<Integer>(Integer.class, this, null, SAMPLE_RATE, this.getPipe()::getSampleRate, null, this.getPipe()::setSampleRate);
        pipeProperties.add(sampleRateProp);

        for (int i = 0; i < this.getPipe().getNumOfChannels(); i++) {
            IPort outputPort = this.getPipe().getOutputPorts().get(i);
            IPort inputPort = this.getPipe().getInputPorts().get(i);
            String propertyName = String.format("Channel %d", i+1);
            PipePropertyRepr<?> pipeProperty = new PipePropertyRepr<>(null, this, inputPort, propertyName, null, outputPort);
            pipeProperties.add(pipeProperty);
        }

        PipePropertyRepr resolutionProp = new PipePropertyRepr<Integer>(Integer.class, this, null, RESOLUTION, this.getPipe()::getResolution, null, this.getPipe()::setResolution);
        pipeProperties.add(resolutionProp);

        PipePropertyRepr lowFrequencyProp = new PipePropertyRepr<Float>(Float.class, this, null, LOW_FREQUENCY, this.getPipe()::getLowFrequency, null, this.getPipe()::setLowFrequency);
        pipeProperties.add(lowFrequencyProp);

        PipePropertyRepr highFrequencyProp = new PipePropertyRepr<Float>(Float.class, this, null, HIGH_FREQUENCY, this.getPipe()::getHighFrequency, null, this.getPipe()::setHighFrequency);
        pipeProperties.add(highFrequencyProp);

        return pipeProperties;
    }

}
