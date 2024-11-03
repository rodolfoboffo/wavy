package com.terpomo.wavy.ui.awt.pipes;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.terpomo.wavy.flow.IPort;

public class PortRepr extends Canvas {

	private static final long serialVersionUID = -4561025309986054679L;

//	private IPort port;
//	private IPipeRepr parentRepr;
	
	public PortRepr(IPort port, IPipeRepr parentRepr) {
		super();
//		this.port = port;
//		this.parentRepr = parentRepr;
		this.setSize(11, 11);
		this.addMouseListener(new PortMouseListener());
		this.addMouseMotionListener(new PortMouseMotionListener());
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawRoundRect(0, 0, 10, 10, 3, 3);
	}
	
	class PortMouseMotionListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class PortMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("Mouse pressed on Port.");
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("Mouse release on Port.");
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			System.out.println("Mouse entered on Port.");
		}

		@Override
		public void mouseExited(MouseEvent e) {
			System.out.println("Mouse exited on Port.");
		}
		
	}
	
}
