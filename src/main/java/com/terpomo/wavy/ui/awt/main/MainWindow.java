package com.terpomo.wavy.ui.awt.main;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.MenuBar;

import com.terpomo.wavy.ui.awt.DefaultWindowListener;

public class MainWindow extends Frame {

	private static final long serialVersionUID = 6740298435235868036L;
	private static final String WAVY = "Wavy";
	protected MenuBar mainMenu;
	protected LayoutManager layout;
	protected ProjectRepr mainProject;
	
	public MainWindow() {
		super(WAVY);
		this.setSize(800, 600);
		this.addWindowListener(new DefaultWindowListener(this));
		
		this.mainMenu = new MainMenuBar();
		this.setMenuBar(mainMenu);
		
		this.layout = new BorderLayout();
		this.setLayout(this.layout);
		this.mainProject = new ProjectRepr();
		this.add(BorderLayout.CENTER, this.mainProject);
	}
	
	public void showApp() {
		this.setVisible(true);
	}
	
}
