package com.terpomo.wavy.ui;

import com.terpomo.wavy.WavyDisposable;
import com.terpomo.wavy.ui.frames.MainWindow;

public class UIApplication implements WavyDisposable {
	
	protected MainWindow main;
	
	public void start() {
		this.main = new MainWindow();
		this.main.showApp();
	}

	@Override
	public void wavyDispose() {
		this.main.dispose();
	}

	public static void main(String[] args) {
		UIApplication app = new UIApplication();
		app.start();
	}
	
}
