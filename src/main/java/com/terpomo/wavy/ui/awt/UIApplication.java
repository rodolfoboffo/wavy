package com.terpomo.wavy.ui.awt;

import java.awt.Frame;

public class UIApplication {
	
	protected Frame frame;
	
	public UIApplication() {
		this.frame = new Frame("Wavy");
		this.frame.setSize(800, 600);
		this.frame.addWindowListener(new DefaultWindowsListener(this.frame));
	}
	
	public void show() {
		this.frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		UIApplication app = new UIApplication();
		app.show();
	}
	
}
