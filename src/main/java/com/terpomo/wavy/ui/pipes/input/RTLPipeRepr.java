package com.terpomo.wavy.ui.pipes.input;

import com.terpomo.wavy.pipes.input.RTLPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RTLPipeRepr extends AbstractPipeRepr<RTLPipe> {

    private static final String Q_CHANNEL = "In-Phase Output";
    private static final String I_CHANNEL = "Quadrature Output";
    private static final String DEVICE = "Device";
    private static final String CENTER_FREQUENCY = "Center Frequency (Hz)";
    private JPanel pipePropertiesPanel;
    private JPanel deviceSelectionPanel;
    private JLabel labelDevice;
    private JComboBox<String> comboDeviceNames;

    public RTLPipeRepr(RTLPipe pipe, String name) {
        super(pipe, name);
        this.createPanels();
        this.createDeviceSelectionPanelControls();
        this.buildCustomPipeControls();
    }

    private void createPanels() {
        GridBagLayout contentLayout = new GridBagLayout();
        JPanel contentPanel = getContentPanel();
        contentPanel.setLayout(contentLayout);

        GridBagLayout outputDirLayout = new GridBagLayout();
        this.deviceSelectionPanel = new JPanel();
        this.deviceSelectionPanel.setLayout(outputDirLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        contentPanel.add(this.deviceSelectionPanel, constraints);

        GridBagLayout pipePropertiesLayout = new GridBagLayout();
        this.pipePropertiesPanel = new JPanel();
        this.pipePropertiesPanel.setLayout(pipePropertiesLayout);
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPanel.add(this.pipePropertiesPanel, constraints);
    }

    private void createDeviceSelectionPanelControls() {
        GridBagConstraints constraints;

        this.labelDevice = new JLabel(DEVICE);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        this.deviceSelectionPanel.add(this.labelDevice, constraints);

        this.comboDeviceNames = new JComboBox<String>(this.getPipe().getDeviceNames());
        this.comboDeviceNames.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> combo = (JComboBox<String>) e.getSource();
                RTLPipeRepr.this.getPipe().setDeviceIndex(combo.getSelectedIndex());
            }
        });
        if (this.comboDeviceNames.getItemCount() > 0)
            this.comboDeviceNames.setSelectedIndex(0);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        this.deviceSelectionPanel.add(this.comboDeviceNames, constraints);
    }

    @SuppressWarnings("rawtypes")
    private void buildCustomPipeControls() {
        List<PipePropertyRepr> propertyControls = this.createPipePropertiesForInputs();
        this.layoutPipePropertiesOnGrid(this.pipePropertiesPanel, propertyControls);
    }

    @SuppressWarnings("rawtypes")
    protected List<PipePropertyRepr> createPipePropertiesForInputs() {
        List<PipePropertyRepr> pipeProperties = new ArrayList<>();

        PipePropertyRepr<Integer> sampleRatePropertyRepr = new PipePropertyRepr<Integer>(Integer.class, this, null, SAMPLE_RATE, this.getPipe()::getSampleRate, null, this.getPipe()::setSampleRate);
        pipeProperties.add(sampleRatePropertyRepr);

        PipePropertyRepr<Long> centerFreqPropertyRepr = new PipePropertyRepr<Long>(Long.class, this, null, CENTER_FREQUENCY, this.getPipe()::getCenterFrequency, null, this.getPipe()::setCenterFrequency);
        pipeProperties.add(centerFreqPropertyRepr);

        PipePropertyRepr iPortProperty = new PipePropertyRepr<>(null, this, null, I_CHANNEL, null, this.getPipe().getIOutputPort());
        pipeProperties.add(iPortProperty);

        PipePropertyRepr qPortProperty = new PipePropertyRepr<>(null, this, null, Q_CHANNEL, null, this.getPipe().getQOutputPort());
        pipeProperties.add(qPortProperty);

        return pipeProperties;
    }
}
