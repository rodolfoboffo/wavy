package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.math.Point;
import com.terpomo.wavy.oscilloscope.TimeValuePair;
import com.terpomo.wavy.pipes.FFTPipe;
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

public class FFTPipeRepr extends AbstractPipeRepr<FFTPipe> {

	private static final String DEFINITION = "Definition";
	private final JFreeChart lineChart;
	private final LayoutManager contentLayout;
	private final JPanel inputPanel;
	private final ChartPanel chartPanel;
	private final XYSeriesCollection dataset;
	private List<XYSeries> channelSeries;
	private List<XYSeries> channelSwapSeries;
	private final RepeatableTask updaterWorker;

	public FFTPipeRepr(FFTPipe pipe, String name) {
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
			List<Point> content = this.getPipe().getValuesForChannel(i);
			if (content == null) continue;
			this.channelSwapSeries.get(i).clear();
			for (Point point : content) {
				this.channelSwapSeries.get(i).add(point.getX(), point.getY());
			}
		}
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				List<XYSeries> auxSeries = FFTPipeRepr.this.channelSeries;
				FFTPipeRepr.this.channelSeries = FFTPipeRepr.this.channelSwapSeries;
				FFTPipeRepr.this.channelSwapSeries = auxSeries;
				FFTPipeRepr.this.dataset.removeAllSeries();
				for (int j = 0; j < FFTPipeRepr.this.getPipe().getNumberOfChannels(); j++) {
					FFTPipeRepr.this.dataset.addSeries(FFTPipeRepr.this.channelSeries.get(j));
				}
			}
		});
	}

	@SuppressWarnings("rawtypes")
	protected List<PipePropertyRepr> createPipePropertiesForInputs() {
		List<PipePropertyRepr> pipeProperties = new ArrayList<>();
		
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
					FFTPipeRepr.this.buildChannels();
					FFTPipeRepr.this.buildCustomPipeControls();
					FFTPipeRepr.this.revalidate();
					FFTPipeRepr.this.repaint();
				}
			});
		}
	}
}
