package com.terpomo.wavy.ui.awt.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JPanel;

import com.terpomo.wavy.Constants;

public class WavyPanel extends JPanel {

	private static final long serialVersionUID = -6911239189205062048L;
	private static final int DEFAULT_INSET_SIZE = 0;
	private Insets i;
	
	public WavyPanel() {
		this(DEFAULT_INSET_SIZE);
		this.setBackground(Constants.LIGHT_CYAN);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics g2d = g.create();
		Dimension s = this.getSize();
		g2d.setColor(this.getBackground());
		g2d.fillRect(0, 0, s.width, s.height);
		g2d.dispose();
	}
	
	public WavyPanel(int padding) {
		this.i = new Insets(padding, padding, padding, padding);
	}
	
	public WavyPanel(int top, int bottom, int left, int right) {
		this.i = new Insets(top, left, bottom, right);
	}
	
	@Override
	public Insets getInsets() {
		return this.i;
	}
}
