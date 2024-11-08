package com.terpomo.wavy.ui.awt;

import com.terpomo.wavy.ui.awt.frames.MainWindow;

public class UIApplication {
	
	protected MainWindow main;
	
	public void start() {
		this.main = new MainWindow();
		this.main.showApp();
	}
	
	public static void main(String[] args) {
		UIApplication app = new UIApplication();
		app.start();
	}
	
}
