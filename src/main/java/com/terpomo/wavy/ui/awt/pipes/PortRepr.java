package com.terpomo.wavy.ui.awt.pipes;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.ui.awt.UIController;

public class PortRepr extends Canvas {

	private static final long serialVersionUID = -4561025309986054679L;

	private IPort port;
	private IPipeRepr parentRepr;
	private DataFlowRepr dataFlowRepr;
	private Color bgColor;
	
	public PortRepr(IPort port, IPipeRepr parentRepr) {
		super();
		this.port = port;
		this.parentRepr = parentRepr;
		this.bgColor = this.port instanceof OutputPort ? Color.YELLOW : Color.RED;
		this.setSize(11, 11);
		this.addMouseListener(new PortMouseListener());
	}
	
	public IPort getPort() {
		return port;
	}
	
	public void setDataFlowRepr(DataFlowRepr dataFlowRepr) {
		this.dataFlowRepr = dataFlowRepr;
	}
	
	public DataFlowRepr getDataFlowRepr() {
		return dataFlowRepr;
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(this.bgColor);
		g2d.fillRoundRect(0, 0, 10, 10, 3, 3);
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect(0, 0, 10, 10, 3, 3);
		g2d.dispose();
	}
	
	class PortMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			UIController.getInstance().portMouseReleased(PortRepr.this);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			UIController.getInstance().setPortBeingHovered(PortRepr.this);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			UIController.getInstance().setPortBeingHovered(null);
		}
		
	}
	
}
