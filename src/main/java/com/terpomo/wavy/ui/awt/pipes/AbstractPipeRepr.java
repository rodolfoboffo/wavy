package com.terpomo.wavy.ui.awt.pipes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.ui.awt.components.WavyPanel;
import com.terpomo.wavy.ui.awt.util.PointOperation;

public abstract class AbstractPipeRepr extends WavyPanel implements IPipeRepr {

	private static final long serialVersionUID = -4460157397034830356L;
	private static final int DEFAULT_INSET_SIZE = 8;
	private static final Color LIGHT_CYAN = new Color(230, 230, 255);
	private static final Font NAME_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	private static final String IS_BEING_MOVED = "IS_BEING_MOVED";
	
	private LayoutManager mainLayout;
	
	protected IPipe pipe;
	private String pipeName;
	private Label labelName;

	protected Panel contentPanel;
	
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
		this.labelName = new Label(this.pipeName);
		this.labelName.setBackground(LIGHT_CYAN);
		this.labelName.setFont(NAME_FONT);
		this.add(BorderLayout.NORTH, this.labelName);
		
		this.contentPanel = new WavyPanel(4);
		this.contentPanel.setBackground(LIGHT_CYAN);
		this.add(BorderLayout.CENTER, this.contentPanel);
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.addMouseListener(new PipeMouseListener());
		this.addMouseMotionListener(new PipeMouseMotionListener());
		this.addPropertyChangeListener(IS_BEING_MOVED, new IsBeingMovedListener());
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(LIGHT_CYAN);
		g.fillRoundRect(0, 0, this.getWidth()-1, this.getHeight()-1, 10, 10);
		g.setColor(Color.BLACK);
		g.drawRoundRect(0, 0, this.getWidth()-1, this.getHeight()-1, 10, 10);
	}
	
	public IPipe getPipe() {
		return pipe;
	}
	
	public void setName(String name) {
		this.pipeName = name;
		this.labelName.setText(this.pipeName);
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
