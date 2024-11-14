package com.terpomo.wavy.ui.awt.pipes;

import java.awt.*;
import java.io.Serial;

import javax.swing.JPanel;

import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.pipes.OscilloscopePipe;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.xy.XYSeriesCollection;

public class OscilloscopePipeRepr extends AbstractPipeRepr {

	@Serial
	private static final long serialVersionUID = 7368297233394330359L;
	private final JFreeChart lineChart;
	private final LayoutManager contentLayout;
	private final JPanel portsPanel;
	private final ChartPanel chartPanel;
	private final XYSeriesCollection dataset;

	public OscilloscopePipeRepr(OscilloscopePipe pipe, String name) {
		super(pipe, name);
		JPanel panel = this.getContentPanel();
		this.contentLayout = new GridBagLayout();
		panel.setLayout(this.contentLayout);
		
		this.portsPanel = new JPanel();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(this.portsPanel, constraints);
		
		this.createPipePropertiesForInputs(pipe);
		this.layoutPipePropertiesOnGrid(this.portsPanel);

		this.dataset = new XYSeriesCollection();
		this.lineChart = ChartFactory.createXYLineChart(null, null, null, this.dataset);
		this.chartPanel = new ChartPanel(this.lineChart);
		this.chartPanel.setLayout(new BorderLayout());
		this.chartPanel.setPreferredSize(new Dimension(400, 200));

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(this.chartPanel, constraints);
	}
	
	protected void createPipePropertiesForInputs(OscilloscopePipe pipe) {
		for (int i = 0; i < pipe.getInputPorts().size(); i++) {
			IPort port = pipe.getInputPorts().get(i);
			String propertyName = String.format("Channel %d", i+1);
			@SuppressWarnings({ "rawtypes", "unchecked" })
			PipePropertyRepr pipeProperty = new PipePropertyRepr(null, this, port, propertyName, null, null);
			this.addPipeProperty(pipeProperty);
		}
	}

}
