package com.terpomo.wavy.ui.awt;

public class UIController {

	private final static UIController instance = new UIController();
	
	private UIController() {}
	
	public static UIController getInstance() {
		return instance;
	}
	
	public void exit() {
		System.exit(0);
	}
	
}
