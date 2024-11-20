package com.terpomo.wavy.ui.pipes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.ui.components.WavyPanel;
import com.terpomo.wavy.ui.util.PointOperation;

public abstract class AbstractPipeRepr extends WavyPanel implements IPipeRepr {

	private static final long serialVersionUID = -4460157397034830356L;
	private static final int DEFAULT_INSET_SIZE = 8;
	private static final String IS_BEING_MOVED = "IS_BEING_MOVED";
	
	private LayoutManager mainLayout;
	
	protected IPipe pipe;
	private String pipeName;
	private JLabel labelName;

	private final JPanel contentPanel;
	
	private boolean isBeingMoved;
	private Point originMousePosition;
	private Point originPipePosition;
	
	public AbstractPipeRepr(IPipe pipe, String name) {
		super(DEFAULT_INSET_SIZE);
		this.pipe = pipe;
		
		this.mainLayout = new BorderLayout();
		this.setLayout(this.mainLayout);
		
		this.isBeingMoved = false;
		this.originMousePosition = null;
		this.originPipePosition = null;
		
		this.pipeName = name;
		this.labelName = new JLabel(this.pipeName);
		this.add(BorderLayout.NORTH, this.labelName);
		
		this.contentPanel = new WavyPanel(4);
		this.add(BorderLayout.CENTER, this.contentPanel);
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.addMouseListener(new PipeMouseListener());
		this.addMouseMotionListener(new PipeMouseMotionListener());
		this.addPropertyChangeListener(IS_BEING_MOVED, new IsBeingMovedListener());
	}
	
	public JPanel getContentPanel() {
		return contentPanel;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(0, 0, this.getWidth()-1, this.getHeight()-1, 10, 10);
        g2d.dispose();
	}
	
	protected void layoutPipePropertiesOnGrid(List<PipePropertyRepr> pipeProperties) {
		this.layoutPipePropertiesOnGrid(this.contentPanel, pipeProperties);
	}

	protected void layoutPipePropertiesOnGrid(Container container, List<PipePropertyRepr> pipeProperties) {
		for (int i = 0; i < pipeProperties.size(); i++) {
			@SuppressWarnings("rawtypes")
			PipePropertyRepr property = pipeProperties.get(i);
			property.layoutOnGrid(container, i);
		}
	}
	
	public IPipe getPipe() {
		return pipe;
	}
	
	public void setName(String name) {
		this.pipeName = name;
		this.labelName.setText(this.pipeName);
	}
	
	public String getPipeName() {
		return pipeName;
	}
	
	public synchronized void setIsBeingMoved(boolean newValue) {
		if (newValue != this.isBeingMoved) {
			boolean oldValue = this.isBeingMoved;
			this.isBeingMoved = newValue;
			this.firePropertyChange(IS_BEING_MOVED, oldValue, newValue);
		}
	}
	
	protected void beginMovingPipe(Point mousePoint) {
		this.originMousePosition = (Point) mousePoint.clone();
		this.originPipePosition = (Point) this.getLocation().clone();
		AbstractPipeRepr.this.setIsBeingMoved(true);
	}
	
	protected void finishMovingPipe(Point mousePoint) {
		AbstractPipeRepr.this.setIsBeingMoved(false);
	}
	
	protected void onDrag(Point mousePoint) {
		if (this.isBeingMoved) {
			Point displace = PointOperation.sub(mousePoint, this.originMousePosition);
			Point destination = PointOperation.sum(displace, this.originPipePosition);
			this.setLocation(destination);
		}
	}
	
	protected void bringToTop() {
		this.getParent().setComponentZOrder(this, 0);
		this.revalidate();
		this.repaint();
	}

	@Override
	public void wavyDispose() {
		Container parent = this.getParent();
		if (parent != null) {
			parent.remove(this);
		}
	}

	class IsBeingMovedListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					if ((boolean)evt.getNewValue()) {
						AbstractPipeRepr.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					}
					else {
						AbstractPipeRepr.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					}
				}
			});
		}
		
	}

	class PipeMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					AbstractPipeRepr.this.bringToTop();
				}
			});
		}

		@Override
		public void mousePressed(MouseEvent e) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					AbstractPipeRepr.this.bringToTop();
					AbstractPipeRepr.this.beginMovingPipe(e.getLocationOnScreen());
				}
			});
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					AbstractPipeRepr.this.finishMovingPipe(e.getLocationOnScreen());
				}
			});
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class PipeMouseMotionListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					AbstractPipeRepr.this.onDrag(e.getLocationOnScreen());
				}
			});
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		}
	}
}
