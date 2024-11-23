package com.terpomo.wavy.ui.pipes;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.ui.UIController;
import com.terpomo.wavy.ui.components.WavyPanel;

import javax.swing.*;

public class PortRepr extends WavyPanel {

	private static final long serialVersionUID = -4561025309986054679L;
	private static final String IS_BEING_HOVERED_PROPERTY = "IS_BEING_HOVERED";
	private static final String IS_SELECTED_PROPERTY = "IS_SELECTED";
	private static final String LINKED_PORT_PROPERTY = "LINKED_PORT";
	private static final Dimension PANEL_DIMENSION = new Dimension(14, 14);
	private static final Dimension PORT_DIMENSION = new Dimension(10, 10);
	private static final Color UNUSED_PORT_COLOR = new Color(200, 200, 200);
	private static final Color LINKED_PORT_COLOR = new Color(20, 200, 20);

	private IPort port;
	private AbstractPipeRepr parentPipeRepr;
	private PortRepr linkedPortRepr;
	private boolean isBeingHovered;
	private boolean isSelected;
	private PortContextMenu contextMenu;
	
	public PortRepr(IPort port, AbstractPipeRepr parentPipeRepr) {
		super();
		this.port = port;
		this.contextMenu = new PortContextMenu();
		this.parentPipeRepr = parentPipeRepr;
		this.isBeingHovered = false;
		this.isSelected = false;
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
	
	public AbstractPipeRepr getParentPipeRepr() {
		return parentPipeRepr;
	}
	
	@Override
	public String getToolTipText() {
		if (this.linkedPortRepr != null)
			this.linkedPortRepr.getParentPipeRepr().getPipeName();
		return null;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g.create();
		if (this.isBeingHovered || this.isSelected) {
			g2d.setColor(Color.BLACK);
			g2d.fillRoundRect(0, 0, PANEL_DIMENSION.width, PANEL_DIMENSION.width, 2, 2);
		}
		Color bgColor = this.getPortColor();
		g2d.setColor(bgColor);
		g2d.fillRoundRect((PANEL_DIMENSION.width-PORT_DIMENSION.width)/2,(PANEL_DIMENSION.height-PORT_DIMENSION.height)/2, PORT_DIMENSION.width, PORT_DIMENSION.height, 2, 2);
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect((PANEL_DIMENSION.width-PORT_DIMENSION.width)/2,(PANEL_DIMENSION.height-PORT_DIMENSION.height)/2, PORT_DIMENSION.width, PORT_DIMENSION.height, 2, 2);
		g2d.dispose();
	}
	
	public Dimension getPreferredSize() {
		return PANEL_DIMENSION;
	}
	
	@Override
	public Dimension getSize() {
		return PANEL_DIMENSION;
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

	private class PortContextMenu extends JPopupMenu {

	}

	private class PortMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				UIController.getInstance().onPortClicked(PortRepr.this);
			} else if (e.getButton() == MouseEvent.BUTTON2) {
				PortRepr.this.contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
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
