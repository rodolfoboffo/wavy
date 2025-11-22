package com.terpomo.wavy.ui;

import com.terpomo.wavy.IWavyDisposable;
import com.terpomo.wavy.ui.frames.MainWindow;

public class UIApplication implements IWavyDisposable {
	
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
