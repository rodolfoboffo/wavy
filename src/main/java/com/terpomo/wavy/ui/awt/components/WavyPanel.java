package com.terpomo.wavy.ui.awt.components;

import java.awt.Insets;
import java.awt.Panel;

public class WavyPanel extends Panel {

	private static final long serialVersionUID = -6911239189205062048L;
	private Insets i;
	
	public WavyPanel() {
		this(0);
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
