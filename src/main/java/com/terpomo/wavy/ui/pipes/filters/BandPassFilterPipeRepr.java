package com.terpomo.wavy.ui.pipes.filters;

import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.pipes.filters.BandPassFilterPipe;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class BandPassFilterPipeRepr extends AbstractPipeRepr<BandPassFilterPipe> {

    private static final String LOW_FREQUENCY = "Low Frequency";
    private static final String HIGH_FREQUENCY = "High Frequency";
    private static final String RESOLUTION = "Resolution";
    private static final String FIR_FILTER = "FIR Filter";
    private JPanel pipePropertiesPanel;
    private XYSeriesCollection dataset;
    private JFreeChart lineChart;
    private ChartPanel chartPanel;
    private XYSeries firFilterSeries;

    public BandPassFilterPipeRepr(BandPassFilterPipe pipe) {
        super(pipe);
        this.createChart();
        this.createPanels();
        this.updateFirFilterChart();
        this.buildCustomPipeControls();
        this.getPipe().addPropertyChangeListener(BandPassFilterPipe.PROPERTY_FIR_FILTER, new BandPassFilterPipeRepr.FirFilterPropertyChangeListener());
    }

    private void updateFirFilterChart() {
        this.firFilterSeries.clear();
        Float[] firFilter = this.getPipe().getFirFilter();
        for (int i = 0; i < firFilter.length; i++) {
            this.firFilterSeries.add(i, firFilter[i]);
        }
    }

    private void createChart() {
        this.dataset = new XYSeriesCollection();
        this.firFilterSeries = new XYSeries(FIR_FILTER);
        this.dataset.addSeries(this.firFilterSeries);
        this.lineChart = ChartFactory.createXYLineChart(null, null, null, this.dataset);
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

        this.chartPanel = new ChartPanel(this.lineChart);
        this.chartPanel.setLayout(new BorderLayout());
        this.chartPanel.setPreferredSize(new Dimension(300, 150));
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPanel.add(this.chartPanel, constraints);
    }

    @SuppressWarnings("rawtypes")
    private void buildCustomPipeControls() {
        List<PipePropertyRepr<?>> propertyControls = this.createPipePropertiesForInputs();
        this.layoutPipePropertiesOnGrid(this.pipePropertiesPanel, propertyControls);
    }

    @SuppressWarnings("rawtypes")
    protected List<PipePropertyRepr<?>> createPipePropertiesForInputs() {
        List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();

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

    class FirFilterPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    BandPassFilterPipeRepr.this.updateFirFilterChart();
                }
            });
        }
    }
}
