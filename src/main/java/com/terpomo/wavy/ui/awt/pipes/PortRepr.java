package com.terpomo.wavy.ui.awt.pipes;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.terpomo.wavy.flow.IPort;

public class PortRepr extends Canvas {

	private static final long serialVersionUID = -4561025309986054679L;
	private static final String IS_BEING_HOVERED_PROPERTY = "IS_BEING_HOVERED";
	private static final String IS_SELECTED_PROPERTY = "IS_SELECTED";
	private static final Stroke ON_HOVER_STROKE = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private static final Stroke DEFAULT_STROKE = new BasicStroke();

	private IPort port;
	private IPipeRepr parentPipeRepr;
	private DataFlowRepr dataFlowRepr;
	private Color bgColor;
	private boolean isBeingHovered;
	private boolean isSelected;
	
	public PortRepr(IPort port, IPipeRepr parentPipeRepr) {
		super();
		this.port = port;
		this.parentPipeRepr = parentPipeRepr;
		this.isBeingHovered = false;
		this.isSelected = false;
		this.setSize(11, 11);
		this.addMouseListener(new PortMouseListener());
		this.addPropertyChangeListener(IS_BEING_HOVERED_PROPERTY, new RepaintOnPropertyChangedListener());
		this.addPropertyChangeListener(IS_SELECTED_PROPERTY, new RepaintOnPropertyChangedListener());
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
		if (this.bgColor != null) {
			g2d.setColor(this.bgColor);
			g2d.fillRoundRect(0, 0, 10, 10, 3, 3);
		}
		Stroke stroke = this.isBeingHovered ? ON_HOVER_STROKE : DEFAULT_STROKE;
		g2d.setStroke(stroke);
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect(0, 0, 10, 10, 3, 3);
		g2d.dispose();
	}
	
	public boolean isBeingHovered() {
		return isBeingHovered;
	}
	
	public void setBeingHovered(boolean isBeingHovered) {
		boolean oldValue = this.isBeingHovered;
		this.isBeingHovered = isBeingHovered;
		this.firePropertyChange(IS_BEING_HOVERED_PROPERTY, oldValue, isBeingHovered);
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void setSelected(boolean isSelected) {
		boolean oldValue = this.isSelected;
		this.isSelected = isSelected;
		this.firePropertyChange(IS_SELECTED_PROPERTY, oldValue, isSelected);
	}
	
	public void toggleSelected() {
		this.setSelected(!this.isSelected());
	}
	
	class RepaintOnPropertyChangedListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					PortRepr.this.revalidate();
					PortRepr.this.repaint();
				}
			});
		}
		
	}
	
	class PortMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			PortRepr.this.toggleSelected();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			PortRepr.this.setBeingHovered(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			PortRepr.this.setBeingHovered(false);
		}
		
	}
	
}
