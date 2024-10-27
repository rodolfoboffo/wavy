package com.terpomo.wavy.ui.awt.pipes;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import com.terpomo.wavy.flow.IPort;

public class PortRepr extends Canvas {

	private static final long serialVersionUID = -4561025309986054679L;

	private IPort port;
	private AbstractPipeRepr parentRepr;
	
	public PortRepr(IPort port, AbstractPipeRepr parentRepr) {
		super();
		this.port = port;
		this.parentRepr = parentRepr;
		
		this.setSize(11, 11);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawRoundRect(0, 0, 10, 10, 3, 3);
	}
	
}
