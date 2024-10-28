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
	private static final String NEW_PROJECT = "New Project";
	private static final String EXIT = "Exit";
	private static final String PIPES = "Pipes";
	private static final String SINE_WAVE = "Sine Wave";
	
	private Menu fileMenu;
	private MenuItem newProjectMenuItem;
	private ActionListener newProjectActionListener;
	private MenuItem exitMenuItem;
	private ActionListener exitActionListener;
	
	private Menu pipesMenu;
	private MenuItem sineWaveMenuItem;
	
	public MainMenuBar() {
		super();
		this.fileMenu = new Menu(FILE);
		this.add(fileMenu);
		
		this.newProjectMenuItem = new MenuItem(NEW_PROJECT);
		this.newProjectActionListener = new NewProjectActionListener();
		this.newProjectMenuItem.addActionListener(this.newProjectActionListener);
		this.fileMenu.add(this.newProjectMenuItem);
		
		this.fileMenu.addSeparator();
		
		this.exitMenuItem = new MenuItem(EXIT);
		this.exitActionListener = new ExitActionListener();
		this.exitMenuItem.addActionListener(this.exitActionListener);
		this.fileMenu.add(exitMenuItem);
		
		this.pipesMenu = new Menu(PIPES);
		this.sineWaveMenuItem = new MenuItem(SINE_WAVE);
		this.pipesMenu.add(this.sineWaveMenuItem);
		this.add(this.pipesMenu);
	}
	
	class NewProjectActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UIController.getInstance().createNewProject();
		}
		
	}
	
	class ExitActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UIController.getInstance().exit();
		}
		
	}

}
