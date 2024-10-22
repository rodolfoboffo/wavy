package com.terpomo.wavy.ui.awt.pipes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
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

public abstract class AbstractPipeRepr extends Panel {

	private static final long serialVersionUID = -4460157397034830356L;
	private static final Font NAME_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	private static final String IS_BEING_MOVED = "IS_BEING_MOVED";  
	
	protected IPipe pipe;
	protected Point position;
	protected LayoutManager layout;
	protected String name;
	protected Label labelName;
	protected boolean isBeingMoved;
	
	public AbstractPipeRepr(IPipe pipe, String name) {
		this.pipe = pipe;
		
		this.layout = new BorderLayout();
		this.setBackground(Color.lightGray);
		
		this.name = name;
		this.isBeingMoved = false;
		this.labelName = new Label(this.name);
		this.labelName.setFont(NAME_FONT);
		this.add(BorderLayout.NORTH, this.labelName);
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.addMouseListener(new PipeMouseListener());
		this.addMouseMotionListener(new PipeMouseMotionListener());
		this.addPropertyChangeListener(IS_BEING_MOVED, new IsBeingMovedListener());
	}
	
	public void setName(String name) {
		this.name = name;
		this.labelName.setText(this.name);
	}
	
	public synchronized void setIsBeingMoved(boolean newValue) {
		if (newValue != this.isBeingMoved) {
			boolean oldValue = this.isBeingMoved;
			this.isBeingMoved = newValue;
			this.firePropertyChange(IS_BEING_MOVED, oldValue, newValue);
		}
	}
	
	class IsBeingMovedListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if ((boolean)evt.getNewValue()) {
				AbstractPipeRepr.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}
			else {
				AbstractPipeRepr.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		}
		
	}
	
	class PipeMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			AbstractPipeRepr.this.setIsBeingMoved(true);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			AbstractPipeRepr.this.setIsBeingMoved(false);
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
//			System.out.println(String.format("Mouse dragged. %s", e.getPoint().toString()));
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		}
	}
}
