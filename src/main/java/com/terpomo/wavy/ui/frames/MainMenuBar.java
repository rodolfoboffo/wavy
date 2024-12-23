package com.terpomo.wavy.ui.frames;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.terpomo.wavy.flow.PipeTypeEnum;
import com.terpomo.wavy.ui.UIController;

public class MainMenuBar extends JMenuBar {

	private static final long serialVersionUID = -4210609163259857512L;
	
	private static final String FILE = "File";
	private static final String NEW_PROJECT = "New Project";
	private static final String EXIT = "Exit";
	private static final String PIPES = "Pipes";
	
	private JMenu fileMenu;
	private JMenuItem newProjectMenuItem;
	private ActionListener newProjectActionListener;
	private JMenuItem exitMenuItem;
	private ActionListener exitActionListener;
	
	private List<JMenuItem> newPipeMenuItems;
	private JMenu pipesMenu;
	
	public MainMenuBar() {
		super();
		this.newPipeMenuItems = new ArrayList<JMenuItem>();
		
		this.fileMenu = new JMenu(FILE);
		this.add(fileMenu);
		
		this.newProjectMenuItem = new JMenuItem(NEW_PROJECT);
		this.newProjectActionListener = new NewProjectActionListener();
		this.newProjectMenuItem.addActionListener(this.newProjectActionListener);
		this.fileMenu.add(this.newProjectMenuItem);
		
		this.fileMenu.addSeparator();
		
		this.exitMenuItem = new JMenuItem(EXIT);
		this.exitActionListener = new ExitActionListener();
		this.exitMenuItem.addActionListener(this.exitActionListener);
		this.fileMenu.add(exitMenuItem);
		
		this.pipesMenu = new JMenu(PIPES);
		this.buildPipesMenuItems();
		this.add(this.pipesMenu);
		
		this.onSelectedProjectChange(UIController.getInstance().getSelectedProjectRepr());
		UIController.getInstance().onSelectedProjectChanged(new SelectedProjectChangedListener());
	}
	
	private void buildPipesMenuItems() {
		for (PipeTypeEnum pipeType : PipeTypeEnum.values()) {
			JMenuItem menuItem = new JMenuItem(pipeType.getFriendlyName());
			NewPipeReprActionListener newPipeActionListener = new NewPipeReprActionListener(pipeType);
			menuItem.addActionListener(newPipeActionListener);
			this.newPipeMenuItems.add(menuItem);
			this.pipesMenu.add(menuItem);
		}
	}
	
	public void onSelectedProjectChange(ProjectRepr p) {
		boolean enabled = p != null;
		for (JMenuItem menuItem : this.newPipeMenuItems) {
			menuItem.setEnabled(enabled);
		}
	}
	
	class NewProjectActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UIController.getInstance().createNewProjectRepr();
		}
	}
	
	class ExitActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UIController.getInstance().exit();
		}
	}
	
	class NewPipeReprActionListener implements ActionListener {

		private PipeTypeEnum pipeType;
		
		public NewPipeReprActionListener(PipeTypeEnum pipeType) {
			super();
			this.pipeType = pipeType;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			UIController.getInstance().createPipeRepr(pipeType);
		}
	}
	
	class SelectedProjectChangedListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					MainMenuBar.this.onSelectedProjectChange((ProjectRepr) evt.getNewValue());
				}
			});
		}
	}

}
