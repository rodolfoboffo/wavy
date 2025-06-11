package com.terpomo.wavy.ui.pipes.misc;

import com.terpomo.wavy.pipes.misc.ResamplingPipe;
import com.terpomo.wavy.signals.InterpolationMethod;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ResamplingPipeRepr extends AbstractPipeRepr<ResamplingPipe> {
    
    private static final String INPUT_SAMPLE_RATE = "Input Sample Rate";
    private static final String OUTPUT_SAMPLE_RATE = "Output Sample Rate";
    private static final String INTERPOLATION_METHOD = "Interpolation Method";
    private JPanel interpolationSelectorPanel;
    private JPanel pipePropertiesPanel;

    public ResamplingPipeRepr(ResamplingPipe pipe) {
        super(pipe);

        GridBagLayout contentLayout = new GridBagLayout();
        this.getContentPanel().setLayout(contentLayout);

        this.createPanels();
        this.buildCustomPipeControls();
        this.buildInterpolationSelectorControls();
    }

    private void createPanels() {
        GridBagLayout contentLayout = new GridBagLayout();
        JPanel contentPanel = getContentPanel();
        contentPanel.setLayout(contentLayout);

        GridBagLayout inputFileLayout = new GridBagLayout();
        this.interpolationSelectorPanel = new JPanel();
        this.interpolationSelectorPanel.setLayout(inputFileLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        contentPanel.add(this.interpolationSelectorPanel, constraints);

        GridBagLayout pipePropertiesLayout = new GridBagLayout();
        this.pipePropertiesPanel = new JPanel();
        this.pipePropertiesPanel.setLayout(pipePropertiesLayout);
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPanel.add(this.pipePropertiesPanel, constraints);
    }

    private void buildInterpolationSelectorControls() {
        GridBagConstraints constraints;

        JLabel labelInterpolation = new JLabel(INTERPOLATION_METHOD);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        this.interpolationSelectorPanel.add(labelInterpolation, constraints);

        JComboBox<InterpolationMethod> comboInterpolationMethods = new JComboBox<>(InterpolationMethod.values());
        comboInterpolationMethods.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<InterpolationMethod> combo = (JComboBox<InterpolationMethod>) e.getSource();
                ResamplingPipeRepr.this.getPipe().setInterpolationMethod((InterpolationMethod) combo.getSelectedItem());
            }
        });
        if (this.getPipe().getInterpolationMethod() != null)
            comboInterpolationMethods.setSelectedItem(this.getPipe().getInterpolationMethod());
        else if (comboInterpolationMethods.getItemCount() > 0)
            comboInterpolationMethods.setSelectedIndex(0);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        this.interpolationSelectorPanel.add(comboInterpolationMethods, constraints);
    }

    private void buildCustomPipeControls() {
        List<PipePropertyRepr<?>> pipeProperties = this.createPipeProperties();
        this.layoutPipePropertiesOnGrid(this.pipePropertiesPanel, pipeProperties);
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
