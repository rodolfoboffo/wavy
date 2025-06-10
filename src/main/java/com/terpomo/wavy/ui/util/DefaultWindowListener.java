package com.terpomo.wavy.ui.util;

import com.terpomo.wavy.ui.UIController;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class DefaultWindowListener implements WindowListener {

	Frame frame;
	
	public DefaultWindowListener(Frame f) {
		this.frame = f;
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		UIController.getInstance().exit();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
