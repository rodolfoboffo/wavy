package com.terpomo.wavy.ui.awt.pipes;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.pipes.OscilloscopePipe;

public class OscilloscopePipeRepr extends AbstractPipeRepr {

	private static final long serialVersionUID = 7368297233394330359L;
	private LayoutManager contentLayout;
	private JPanel portsPanel;
	private JPanel chartPanel;
	
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
		
		this.chartPanel = new JPanel();
		this.chartPanel.setLayout(new BorderLayout());
//		this.chartPanel.add(BorderLayout.CENTER, new jfreechar.)
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
