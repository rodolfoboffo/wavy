package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.oscilloscope.TimeValuePair;
import com.terpomo.wavy.pipes.OscilloscopePipe;
import com.terpomo.wavy.ui.util.GuiUpdaterWorker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class OscilloscopePipeRepr extends AbstractPipeRepr {

	@Serial
	private static final long serialVersionUID = 7368297233394330359L;
	private final OscilloscopePipe pipe;
	private final JFreeChart lineChart;
	private final LayoutManager contentLayout;
	private final JPanel portsPanel;
	private final ChartPanel chartPanel;
	private final XYSeriesCollection dataset;
	private XYSeries channel1Series;
	private XYSeries channel1SeriesSwap;
	private final GuiUpdaterWorker updaterWorker;

	public OscilloscopePipeRepr(OscilloscopePipe pipe, String name) {
		super(pipe, name);
		this.pipe = pipe;
		JPanel panel = this.getContentPanel();
		this.contentLayout = new GridBagLayout();
		panel.setLayout(this.contentLayout);
		
		this.portsPanel = new JPanel();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(this.portsPanel, constraints);

		@SuppressWarnings("rawtypes")
		List<PipePropertyRepr> properties = this.createPipePropertiesForInputs(pipe);
		this.layoutPipePropertiesOnGrid(this.portsPanel, properties);
		this.dataset = new XYSeriesCollection();
		this.channel1Series = new XYSeries("Channel 1");
		this.channel1SeriesSwap = new XYSeries("Channel 1");
		this.dataset.addSeries(this.channel1Series);
		this.lineChart = ChartFactory.createXYLineChart(null, null, null, this.dataset);
		this.chartPanel = new ChartPanel(this.lineChart);
		this.chartPanel.setLayout(new BorderLayout());
		this.chartPanel.setPreferredSize(new Dimension(300, 150));

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(this.chartPanel, constraints);

		this.updaterWorker = new GuiUpdaterWorker(this::updateGui, 1000);
		this.updaterWorker.start();
	}

	private void updateGui() {
		List<TimeValuePair> content = this.pipe.getValues();
		this.channel1SeriesSwap.clear();
		for (int i = 0; i < content.size(); i++) {
			TimeValuePair timeValuePair = content.get(i);
			this.channel1SeriesSwap.add(timeValuePair.getTime(), timeValuePair.getValue());
		}
		EventQueue.invokeLater(new PipeUpdater());
	}

	@SuppressWarnings("rawtypes")
	protected List<PipePropertyRepr> createPipePropertiesForInputs(OscilloscopePipe pipe) {
		List<PipePropertyRepr> pipeProperties = new ArrayList<PipePropertyRepr>();
		for (int i = 0; i < pipe.getInputPorts().size(); i++) {
			IPort port = pipe.getInputPorts().get(i);
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
        try {
            this.updaterWorker.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

	class PipeUpdater implements Runnable {

		@Override
		public void run() {
			XYSeries auxSeries = OscilloscopePipeRepr.this.channel1Series;
			OscilloscopePipeRepr.this.channel1Series = OscilloscopePipeRepr.this.channel1SeriesSwap;
			OscilloscopePipeRepr.this.channel1SeriesSwap = auxSeries;
			OscilloscopePipeRepr.this.dataset.removeAllSeries();
			OscilloscopePipeRepr.this.dataset.addSeries(OscilloscopePipeRepr.this.channel1Series);
		}
	}
}