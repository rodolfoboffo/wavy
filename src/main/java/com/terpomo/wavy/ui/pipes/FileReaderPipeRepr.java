package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.pipes.FileReaderPipe;
import com.terpomo.wavy.ui.components.WavyPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileReaderPipeRepr extends AbstractPipeRepr<FileReaderPipe> {

    private static final String PATH = "Path";
    private static final String BROWSE = "Browse";
    private static final Dimension MAX_DIMENSION_FIELD_PATH = new Dimension(200, 50);
    private static final Dimension MIN_DIMENSION_FIELD_PATH = new Dimension(100, 50);
    private JPanel pipePropertiesPanel;
    private JPanel inputFilePanel;
    private JLabel labelPath;
    private JTextField fieldPath;
    private JButton buttonBrowse;
    private JFileChooser fileChooser;

    public FileReaderPipeRepr(FileReaderPipe pipe, String name) {
        super(pipe, name);
        this.createPanels();
        this.createInputFileControls();
        this.buildCustomPipeControls();
        this.getPipe().addPropertyChangeListener(AbstractPipe.PROPERTY_PIPE_OUTPUT_PORTS, new OutputPortsPropertyChangeListener());
    }

    private void createPanels() {
        GridBagLayout contentLayout = new GridBagLayout();
        JPanel contentPanel = getContentPanel();
        contentPanel.setLayout(contentLayout);

        GridBagLayout inputFileLayout = new GridBagLayout();
        this.inputFilePanel = new JPanel();
        this.inputFilePanel.setLayout(inputFileLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        contentPanel.add(this.inputFilePanel, constraints);

        GridBagLayout pipePropertiesLayout = new GridBagLayout();
        this.pipePropertiesPanel = new JPanel();
        this.pipePropertiesPanel.setLayout(pipePropertiesLayout);
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPanel.add(this.pipePropertiesPanel, constraints);
    }

    private void createInputFileControls() {
        GridBagConstraints constraints;

        this.labelPath = new JLabel(PATH);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        this.inputFilePanel.add(this.labelPath, constraints);

        this.fieldPath = new JTextField(this.getPipe().getInputFilePath());
        this.fieldPath.setEnabled(false);
        this.fieldPath.setMaximumSize(MAX_DIMENSION_FIELD_PATH);
        this.fieldPath.setMinimumSize(MIN_DIMENSION_FIELD_PATH);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        this.inputFilePanel.add(this.fieldPath, constraints);

        this.fileChooser = new JFileChooser();
        this.buttonBrowse = new JButton(BROWSE);
        this.buttonBrowse.addActionListener(new BrowseButtonActionListener());
        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        this.inputFilePanel.add(this.buttonBrowse, constraints);
    }

    @SuppressWarnings("rawtypes")
    private void buildCustomPipeControls() {
        List<PipePropertyRepr> propertyControls = this.createPipePropertiesForInputs();
        this.layoutPipePropertiesOnGrid(this.pipePropertiesPanel, propertyControls);
    }

    @SuppressWarnings("rawtypes")
    protected List<PipePropertyRepr> createPipePropertiesForInputs() {
        List<PipePropertyRepr> pipeProperties = new ArrayList<>();

        if (this.getPipe().isFileOpen()) {
            PipePropertyRepr<Integer> sampleRatePropertyRepr = new PipePropertyRepr<Integer>(Integer.class, this, null, SAMPLE_RATE, this.getPipe().getSampleRate(), null, true);
            pipeProperties.add(sampleRatePropertyRepr);

            for (int i = 0; i < this.getPipe().getOutputPorts().size(); i++) {
                IPort port = this.getPipe().getOutputPorts().get(i);
                String propertyName = String.format("Channel %d", i+1);
                PipePropertyRepr<?> pipeProperty = new PipePropertyRepr<>(null, this, null, propertyName, null, port);
                pipeProperties.add(pipeProperty);
            }
        }
        return pipeProperties;
    }

    class BrowseButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int result = FileReaderPipeRepr.this.fileChooser.showOpenDialog(FileReaderPipeRepr.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = FileReaderPipeRepr.this.fileChooser.getSelectedFile();
                FileReaderPipeRepr.this.fieldPath.setText(selectedFile.getPath());
                FileReaderPipeRepr.this.getPipe().setInputFile(selectedFile);
            }
        }
    }

    class OutputPortsPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    FileReaderPipeRepr.this.buildCustomPipeControls();
                    FileReaderPipeRepr.this.revalidate();
                    FileReaderPipeRepr.this.repaint();
                }
            });
        }
    }

}
