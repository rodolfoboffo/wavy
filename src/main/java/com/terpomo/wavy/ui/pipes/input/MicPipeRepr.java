package com.terpomo.wavy.ui.pipes.input;

import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.pipes.input.MicPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MicPipeRepr extends AbstractPipeRepr<MicPipe> {
    private static final Logger LOGGER = Logger.getLogger(MicPipeRepr.class.getName());
    private static final String CHANNEL_NUMBER = "Channel #%d";
    private static final String MIXER = "Mixer";
    private JPanel pipePropertiesPanel;
    private JPanel mixerPanel;
    private JLabel labelMixer;
    private JComboBox<String> comboMixerInfos;

    public MicPipeRepr(MicPipe pipe, String name) {
        super(pipe, name);

        this.createPanels();
        this.createMixerSelectorControls();
        this.buildCustomPipeControls();
    }

    private void createPanels() {
        GridBagLayout contentLayout = new GridBagLayout();
        JPanel contentPanel = getContentPanel();
        contentPanel.setLayout(contentLayout);

        GridBagLayout inputFileLayout = new GridBagLayout();
        this.mixerPanel = new JPanel();
        this.mixerPanel.setLayout(inputFileLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        contentPanel.add(this.mixerPanel, constraints);

        GridBagLayout pipePropertiesLayout = new GridBagLayout();
        this.pipePropertiesPanel = new JPanel();
        this.pipePropertiesPanel.setLayout(pipePropertiesLayout);
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPanel.add(this.pipePropertiesPanel, constraints);
    }

    private void createMixerSelectorControls() {
        GridBagConstraints constraints;

        this.labelMixer = new JLabel(MIXER);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        this.mixerPanel.add(this.labelMixer, constraints);

        this.comboMixerInfos = new JComboBox<String>(this.getPipe().getMixerInfos());
        this.comboMixerInfos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> combo = (JComboBox<String>) e.getSource();
                MicPipeRepr.this.getPipe().setMixer(combo.getSelectedItem().toString());
            }
        });
        if (this.comboMixerInfos.getItemCount() > 0)
            this.comboMixerInfos.setSelectedIndex(0);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        this.mixerPanel.add(this.comboMixerInfos, constraints);
    }

    private void buildCustomPipeControls() {
        java.util.List<PipePropertyRepr<?>> pipeProperties = this.createPipePropertiesForInputs();
        this.layoutPipePropertiesOnGrid(this.pipePropertiesPanel, pipeProperties);
    }

    @SuppressWarnings("rawtypes")
    private java.util.List<PipePropertyRepr<?>> createPipePropertiesForInputs() {
        List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();

        for (int i = 0; i < this.getPipe().getOutputPorts().size(); i++) {
            OutputPort _pipe = this.getPipe().getOutputPorts().get(i);
            PipePropertyRepr signalOutputProperty = new PipePropertyRepr<>(null, this, null, String.format(CHANNEL_NUMBER, i+1), null, _pipe);
            pipeProperties.add(signalOutputProperty);
        }
        return pipeProperties;
    }
}
