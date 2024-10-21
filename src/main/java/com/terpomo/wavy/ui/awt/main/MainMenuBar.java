package com.terpomo.wavy.ui.awt.main;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.terpomo.wavy.ui.awt.UIController;

public class MainMenuBar extends MenuBar {

	private static final long serialVersionUID = -4210609163259857512L;
	
	private static final String FILE = "File";
	private static final String EXIT = "Exit";
	private static final String PIPES = "Pipes";
	private static final String SINE_WAVE = "Sine Wave";
	
	protected Menu fileMenu;
	protected MenuItem exitMenuItem;
	protected ActionListener exitActionListener;
	
	protected Menu pipesMenu;
	protected MenuItem sineWaveMenuItem;
	
	public MainMenuBar() {
		super();
		this.fileMenu = new Menu(FILE);
		this.add(fileMenu);
		
		this.exitMenuItem = new MenuItem(EXIT);
		this.exitActionListener = new ExitActionListener();
		this.exitMenuItem.addActionListener(this.exitActionListener);
		this.fileMenu.add(exitMenuItem);
		
		this.pipesMenu = new Menu(PIPES);
		this.sineWaveMenuItem = new MenuItem(SINE_WAVE);
		this.pipesMenu.add(this.sineWaveMenuItem);
		this.add(this.pipesMenu);
	}
	
	class ExitActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UIController.getInstance().exit();
		}
		
	}

}
