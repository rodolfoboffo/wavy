package com.terpomo.wavy.ui.awt.pipes;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.ui.awt.UIController;

public class PortRepr extends Canvas {

	private static final long serialVersionUID = -4561025309986054679L;
	private static final String IS_BEING_HOVERED_PROPERTY = "IS_BEING_HOVERED";
	private static final String IS_SELECTED_PROPERTY = "IS_SELECTED";
	private static final String LINKED_PORT_PROPERTY = "LINKED_PORT";
	private static final Color UNUSED_PORT_COLOR = new Color(200, 200, 200);
	private static final Color LINKED_PORT_COLOR = new Color(20, 200, 20);

	private Dimension size;
	private IPort port;
	private IPipeRepr parentPipeRepr;
	private PortRepr linkedPortRepr;
	private boolean isBeingHovered;
	private boolean isSelected;
	
	public PortRepr(IPort port, IPipeRepr parentPipeRepr) {
		super();
		this.size = new Dimension(18, 18);
		this.port = port;
		this.parentPipeRepr = parentPipeRepr;
		this.isBeingHovered = false;
		this.isSelected = false;
		this.setSize(11, 11);
		this.addMouseListener(new PortMouseListener());
		this.addPropertyChangeListener(IS_BEING_HOVERED_PROPERTY, new RepaintOnPropertyChangedListener());
		this.addPropertyChangeListener(IS_SELECTED_PROPERTY, new RepaintOnPropertyChangedListener());
		this.addPropertyChangeListener(LINKED_PORT_PROPERTY, new RepaintOnPropertyChangedListener());
	}
	
	public void setLinkedPortRepr(PortRepr linkedPortRepr) {
		this.linkedPortRepr = linkedPortRepr;
	}
	
	public PortRepr getLinkedPortRepr() {
		return linkedPortRepr;
	}
	
	public IPort getPort() {
		return port;
	}
	
	private Color getPortColor() {
		if (this.linkedPortRepr == null) {
			return UNUSED_PORT_COLOR;
		}
		else {
			return LINKED_PORT_COLOR;
		}
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
		if (this.isBeingHovered || this.isSelected) {
			g2d.setColor(Color.BLACK);
			g2d.fillRoundRect(2, 2, 14, 14, 4, 4);
		}
		Color bgColor = this.getPortColor();
		g2d.setColor(bgColor);
		g2d.fillRoundRect(4, 4, 10, 10, 2, 2);
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect(4, 4, 10, 10, 2, 2);
		g2d.dispose();
	}
	
	@Override
	public Dimension getSize() {
		return this.size;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return this.getSize();
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
			UIController.getInstance().onPortClicked(PortRepr.this);
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
