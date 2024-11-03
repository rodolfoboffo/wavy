package com.terpomo.wavy.ui.awt.components;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Panel;

public class WavyPanel extends Panel {

	private static final long serialVersionUID = -6911239189205062048L;
	private static final int DEFAULT_INSET_SIZE = 0;
	private Insets i;
	
	public WavyPanel() {
		this(DEFAULT_INSET_SIZE);
	}
	
	public WavyPanel(int padding) {
		this.i = new Insets(padding, padding, padding, padding);
	}
	
	public WavyPanel(int top, int bottom, int left, int right) {
		this.i = new Insets(top, left, bottom, right);
	}

	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return super.getPreferredSize();
	}
	
	@Override
	public Insets getInsets() {
		return this.i;
	}
}
