package com.terpomo.wavy.ui.pipes.output;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.pipes.output.FileWriterPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileWriterPipeRepr extends AbstractPipeRepr<FileWriterPipe> {

    private static final String DIRECTORY = "Directory";
    private static final String BROWSE = "Browse";
    private static final Dimension MAX_DIMENSION_FIELD_PATH = new Dimension(200, 50);
    private static final Dimension MIN_DIMENSION_FIELD_PATH = new Dimension(100, 50);
    private static final String NUM_CHANNELS = "# of Channels";
    private JPanel pipePropertiesPanel;
    private JPanel outputDirPanel;
    private JLabel labelPath;
    private JTextField fieldPath;
    private JButton buttonBrowse;
    private JFileChooser fileChooser;

    public FileWriterPipeRepr(FileWriterPipe pipe, String name) {
        super(pipe, name);
        this.createPanels();
        this.createOutputDirControls();
        this.buildCustomPipeControls();
        this.getPipe().addPropertyChangeListener(AbstractPipe.PROPERTY_PIPE_INPUT_PORTS, new InputPortsPropertyChangeListener());
    }

    private void createPanels() {
        GridBagLayout contentLayout = new GridBagLayout();
        JPanel contentPanel = getContentPanel();
        contentPanel.setLayout(contentLayout);

        GridBagLayout outputDirLayout = new GridBagLayout();
        this.outputDirPanel = new JPanel();
        this.outputDirPanel.setLayout(outputDirLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        contentPanel.add(this.outputDirPanel, constraints);

        GridBagLayout pipePropertiesLayout = new GridBagLayout();
        this.pipePropertiesPanel = new JPanel();
        this.pipePropertiesPanel.setLayout(pipePropertiesLayout);
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPanel.add(this.pipePropertiesPanel, constraints);
    }

    private void createOutputDirControls() {
        GridBagConstraints constraints;

        this.labelPath = new JLabel(DIRECTORY);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        this.outputDirPanel.add(this.labelPath, constraints);

        this.fieldPath = new JTextField(this.getPipe().getOutputDirectory());
        this.fieldPath.setEnabled(false);
        this.fieldPath.setMaximumSize(MAX_DIMENSION_FIELD_PATH);
        this.fieldPath.setMinimumSize(MIN_DIMENSION_FIELD_PATH);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        this.outputDirPanel.add(this.fieldPath, constraints);

        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.buttonBrowse = new JButton(BROWSE);
        this.buttonBrowse.addActionListener(new BrowseButtonActionListener());
        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        this.outputDirPanel.add(this.buttonBrowse, constraints);
    }

    @SuppressWarnings("rawtypes")
    private void buildCustomPipeControls() {
        List<PipePropertyRepr<?>> propertyControls = this.createPipePropertiesForInputs();
        this.layoutPipePropertiesOnGrid(this.pipePropertiesPanel, propertyControls);
    }

    @SuppressWarnings("rawtypes")
    protected List<PipePropertyRepr<?>> createPipePropertiesForInputs() {
        List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();

        PipePropertyRepr<Integer> sampleRatePropertyRepr = new PipePropertyRepr<Integer>(Integer.class, this, null, SAMPLE_RATE, this.getPipe()::getSampleRate, null, this.getPipe()::setSampleRate);
        pipeProperties.add(sampleRatePropertyRepr);

        PipePropertyRepr<Integer> numChannelsPropRepr = new PipePropertyRepr<Integer>(Integer.class, this, null, NUM_CHANNELS, this.getPipe()::getNumOfChannels, null, this.getPipe()::setNumOfChannels);
        pipeProperties.add(numChannelsPropRepr);

        for (int i = 0; i < this.getPipe().getInputPorts().size(); i++) {
            IPort port = this.getPipe().getInputPorts().get(i);
            String propertyName = String.format("Channel %d", i+1);
            PipePropertyRepr<?> pipeProperty = new PipePropertyRepr<>(null, this, port, propertyName, null, null);
            pipeProperties.add(pipeProperty);
        }

        return pipeProperties;
    }

    class BrowseButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int result = FileWriterPipeRepr.this.fileChooser.showOpenDialog(FileWriterPipeRepr.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = FileWriterPipeRepr.this.fileChooser.getSelectedFile();
                FileWriterPipeRepr.this.fieldPath.setText(selectedFile.getPath());
                FileWriterPipeRepr.this.getPipe().setOutputDirectory(selectedFile);
                FileWriterPipeRepr.this.revalidate();
                FileWriterPipeRepr.this.repaint();
            }
        }
    }

    class InputPortsPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    FileWriterPipeRepr.this.buildCustomPipeControls();
                    FileWriterPipeRepr.this.revalidate();
                    FileWriterPipeRepr.this.repaint();
                }
            });
        }
    }

}
