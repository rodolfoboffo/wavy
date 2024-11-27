package com.terpomo.wavy.ui.pipes;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.terpomo.wavy.core.IWavyModel;
import com.terpomo.wavy.flow.IPort;
import com.terpomo.wavy.ui.UIController;
import com.terpomo.wavy.ui.components.WavyPanel;
import com.terpomo.wavy.ui.components.IWavyRepr;
import com.terpomo.wavy.util.RepeatableTask;

import javax.swing.*;

public class PortRepr extends WavyPanel implements IWavyRepr {

	private static final long serialVersionUID = -4561025309986054679L;
	private static final String IS_BEING_HOVERED_PROPERTY = "IS_BEING_HOVERED";
	private static final String IS_SELECTED_PROPERTY = "IS_SELECTED";
	private static final String LINKED_PORT_REPR_PROPERTY = "LINKED_PORT";
	private static final String IS_BUFFER_FULL_PROPERTY = "IS_BUFFER_FULL_PROPERTY";
	private static final Dimension PANEL_DIMENSION = new Dimension(14, 14);
	private static final Dimension PORT_DIMENSION = new Dimension(10, 10);
	private static final Color UNUSED_PORT_COLOR = new Color(200, 200, 200);
	private static final Color LINKED_PORT_COLOR = new Color(20, 200, 20);
	private static final Color FULL_BUFFER_PORT_COLOR = new Color(200, 10, 10);
	private final RepeatableTask updaterWorker;

	private final IPort port;
	private final AbstractPipeRepr<?> parentPipeRepr;
	private PortRepr linkedPortRepr;
	private boolean isBeingHovered;
	private boolean isSelected;
	private final PortContextMenu contextMenu;
	private boolean isBufferFull;

	public PortRepr(IPort port, AbstractPipeRepr<?> parentPipeRepr) {
		super();
		this.port = port;
		UIController.getInstance().addModelToReprMapEntry(this.port, this);
		this.isBufferFull = this.port.getBuffer().isFull();
		this.contextMenu = new PortContextMenu();
		this.parentPipeRepr = parentPipeRepr;
		this.isBeingHovered = false;
		this.isSelected = false;
		this.addMouseListener(new PortMouseListener());
		this.addPropertyChangeListener(IS_BEING_HOVERED_PROPERTY, new RepaintOnPropertyChangedListener());
		this.addPropertyChangeListener(IS_SELECTED_PROPERTY, new RepaintOnPropertyChangedListener());
		this.addPropertyChangeListener(LINKED_PORT_REPR_PROPERTY, new LinkedPortReprPropertyChangedListener());
		this.port.addPropertyChangeListener(IPort.LINKED_PORT_PROPERTY, new LinkedPortPropertyChangedListener());
		this.port.addPropertyChangeListener(IS_BUFFER_FULL_PROPERTY, new RepaintOnPropertyChangedListener());

		this.setLinkedPortRepr((PortRepr) UIController.getInstance().getReprFromModelObj(port.getLinkedPort()));
		this.updaterWorker = new RepeatableTask(this::updatePortState, 100);
		this.updaterWorker.start();
	}

	private void updatePortState() {
		this.setBufferFull(this.port.getBuffer().isFull());
	}

	private void setBufferFull(boolean bufferFull) {
		boolean oldValue = this.isBufferFull;
		isBufferFull = bufferFull;
		this.firePropertyChange(IS_BUFFER_FULL_PROPERTY, oldValue, bufferFull);
	}

	private void setTooltipForLinkedPort(PortRepr linkedPortRepr) {
		if (linkedPortRepr != null) {
			this.setToolTipText(linkedPortRepr.getParentPipeRepr().getPipeName());
		}
		else {
			this.setToolTipText(null);
		}
	}

	public void setLinkedPortRepr(PortRepr linkedPortRepr) {
		PortRepr oldValue = this.linkedPortRepr;
		this.linkedPortRepr = linkedPortRepr;
		this.firePropertyChange(LINKED_PORT_REPR_PROPERTY, oldValue, linkedPortRepr);
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
			if (this.port.getBuffer().isFull())
				return LINKED_PORT_COLOR;
			else
				return FULL_BUFFER_PORT_COLOR;
		}
	}
	
	public AbstractPipeRepr<?> getParentPipeRepr() {
		return parentPipeRepr;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g.create();
		if (this.isBeingHovered || this.isSelected) {
			g2d.setColor(Color.BLACK);
			g2d.fillRoundRect(0, 0, PANEL_DIMENSION.width, PANEL_DIMENSION.height, 2, 2);
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

	class LinkedPortPropertyChangedListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			IPort modelObj = (IPort) evt.getNewValue();
			PortRepr.this.setLinkedPortRepr((PortRepr) UIController.getInstance().getReprFromModelObj(modelObj));
		}
	}

	class RepaintRunnable implements Runnable {

		@Override
		public void run() {
			PortRepr.this.revalidate();
			PortRepr.this.repaint();
		}
	}

	class LinkedPortReprPropertyChangedListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			PortRepr _linkedPortRepr = (PortRepr) evt.getNewValue();
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					PortRepr.this.setTooltipForLinkedPort(_linkedPortRepr);
				}
			});
		}
	}

	class RepaintOnPropertyChangedListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			EventQueue.invokeLater(new RepaintRunnable());
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
