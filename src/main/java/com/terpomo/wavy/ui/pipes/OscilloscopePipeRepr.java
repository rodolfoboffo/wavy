package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.oscilloscope.TimeValuePair;
import com.terpomo.wavy.pipes.OscilloscopePipe;
import com.terpomo.wavy.util.RepeatableTask;
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

public class OscilloscopePipeRepr extends AbstractPipeRepr<OscilloscopePipe> {

	private static final String NUMBER_OF_CHANNELS = "# of Channels";
	private static final String DEFINITION = "Definition";
	private static final String SCALE = "Scale";
	private static final long serialVersionUID = 7368297233394330359L;
	private final JFreeChart lineChart;
	private final LayoutManager contentLayout;
	private final JPanel inputPanel;
	private final ChartPanel chartPanel;
	private final XYSeriesCollection dataset;
	private List<XYSeries> channelSeries;
	private List<XYSeries> channelSwapSeries;
	private final RepeatableTask updaterWorker;

	public OscilloscopePipeRepr(OscilloscopePipe pipe, String name) {
		super(pipe, name);
		JPanel panel = this.getContentPanel();
		this.contentLayout = new GridBagLayout();
		panel.setLayout(this.contentLayout);
		
		this.inputPanel = new JPanel();
		this.inputPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(this.inputPanel, constraints);

		this.buildCustomPipeControls();
		this.getPipe().addPropertyChangeListener(AbstractPipe.PROPERTY_PIPE_INPUT_PORTS, new InputPortsPropertyChangeListener());

		this.dataset = new XYSeriesCollection();
		this.channelSeries = new ArrayList<>();
		this.channelSwapSeries = new ArrayList<>();
		this.buildChannels();
		this.lineChart = ChartFactory.createXYLineChart(null, null, null, this.dataset);
		this.chartPanel = new ChartPanel(this.lineChart);
		this.chartPanel.setLayout(new BorderLayout());
		this.chartPanel.setPreferredSize(new Dimension(300, 150));

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(this.chartPanel, constraints);

		this.updaterWorker = new RepeatableTask(this::updateGui, 100);
		this.updaterWorker.start();
	}

	@SuppressWarnings("rawtypes")
    synchronized private void buildCustomPipeControls() {
		List<PipePropertyRepr> pipeProperties = this.createPipePropertiesForInputs();
		this.layoutPipePropertiesOnGrid(this.inputPanel, pipeProperties);
	}

	synchronized void buildChannels() {
		this.channelSeries.clear();
		this.channelSwapSeries.clear();
		this.dataset.removeAllSeries();
		for (int i = 0; i < this.getPipe().getNumberOfChannels(); i++) {
			this.channelSeries.add(new XYSeries(String.format("Channel %d", i+1)));
			this.channelSwapSeries.add(new XYSeries(String.format("Channel %d", i+1)));
			this.dataset.addSeries(this.channelSeries.get(i));
		}
	}

	synchronized private void updateGui() {
		for (int i = 0; i < this.getPipe().getNumberOfChannels(); i++) {
			List<TimeValuePair> content = this.getPipe().getValuesForChannel(i);
			this.channelSwapSeries.get(i).clear();
			for (TimeValuePair timeValuePair : content) {
				this.channelSwapSeries.get(i).add(timeValuePair.getTimeInMillisec(), timeValuePair.getValue());
			}
		}
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				List<XYSeries> auxSeries = OscilloscopePipeRepr.this.channelSeries;
				OscilloscopePipeRepr.this.channelSeries = OscilloscopePipeRepr.this.channelSwapSeries;
				OscilloscopePipeRepr.this.channelSwapSeries = auxSeries;
				OscilloscopePipeRepr.this.dataset.removeAllSeries();
				for (int j = 0; j < OscilloscopePipeRepr.this.getPipe().getNumberOfChannels(); j++) {
					OscilloscopePipeRepr.this.dataset.addSeries(OscilloscopePipeRepr.this.channelSeries.get(j));
				}
			}
		});
	}

	@SuppressWarnings("rawtypes")
	protected List<PipePropertyRepr> createPipePropertiesForInputs() {
		List<PipePropertyRepr> pipeProperties = new ArrayList<>();

		PipePropertyRepr<Integer> sampleRateProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, SAMPLE_RATE, this.getPipe().getSampleRate(), null, this.getPipe()::setSampleRate);
		pipeProperties.add(sampleRateProperty);

		PipePropertyRepr<Integer> numOfChannelsProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, NUMBER_OF_CHANNELS, this.getPipe().getNumberOfChannels(), null, this.getPipe()::setNumberOfChannels);
		pipeProperties.add(numOfChannelsProperty);

		PipePropertyRepr<Float> definitionProperty = new PipePropertyRepr<Float>(Float.class, this, null, DEFINITION, this.getPipe().getQuality(), null, this.getPipe()::setQuality);
		pipeProperties.add(definitionProperty);

		PipePropertyRepr<Float> scaleProperty = new PipePropertyRepr<Float>(Float.class, this, null, SCALE, this.getPipe().getScale(), null, this.getPipe()::setScale);
		pipeProperties.add(scaleProperty);
		
		for (int i = 0; i < this.getPipe().getInputPorts().size(); i++) {
			IPort port = this.getPipe().getInputPorts().get(i);
			String propertyName = String.format("Channel %d", i+1);
			@SuppressWarnings({ "rawtypes", "unchecked" })
			PipePropertyRepr pipeProperty = new PipePropertyRepr(null, this, port, propertyName, null, null);
			pipeProperties.add(pipeProperty);
		}
		return pipeProperties;
	}

	@Override
	public void wavyDispose() {
		super.wavyDispose();
		this.updaterWorker.wavyDispose();
    }

	class InputPortsPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					OscilloscopePipeRepr.this.buildChannels();
					OscilloscopePipeRepr.this.buildCustomPipeControls();
					OscilloscopePipeRepr.this.revalidate();
					OscilloscopePipeRepr.this.repaint();
				}
			});
		}
	}
}
