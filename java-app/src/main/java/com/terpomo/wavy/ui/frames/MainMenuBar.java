package com.terpomo.wavy.ui.frames;

import com.terpomo.wavy.pipes.PipeTypeEnum;
import com.terpomo.wavy.ui.UIController;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainMenuBar extends JMenuBar {

	private static final long serialVersionUID = -4210609163259857512L;
	
	private static final String FILE = "File";
	private static final String NEW_PROJECT = "New Project";
	private static final String OPEN_PROJECT = "Open Project";
	private static final String SAVE_PROJECT = "Save Project";
	private static final String EXIT = "Exit";
	private static final String PIPES = "Pipes";

	private final JMenu fileMenu;
	private final JMenuItem newProjectMenuItem;
	private final JMenuItem openProjectMenuItem;
	private final JMenuItem saveProjectMenuItem;
	private final JMenuItem exitMenuItem;
	private final JFileChooser projectFileChooser;
	
	private List<JMenuItem> newPipeMenuItems;
	private JMenu pipesMenu;
	
	public MainMenuBar() {
		super();
		this.newPipeMenuItems = new ArrayList<JMenuItem>();

		this.projectFileChooser = new JFileChooser();
		this.projectFileChooser.setMultiSelectionEnabled(false);

		this.fileMenu = new JMenu(FILE);
		this.add(fileMenu);
		
		this.newProjectMenuItem = new JMenuItem(NEW_PROJECT);
		this.newProjectMenuItem.addActionListener(new NewProjectActionListener());
		this.fileMenu.add(this.newProjectMenuItem);

		this.openProjectMenuItem = new JMenuItem(OPEN_PROJECT);
		this.openProjectMenuItem.addActionListener(new OpenProjectActionListener());
		this.fileMenu.add(this.openProjectMenuItem);

		this.saveProjectMenuItem = new JMenuItem(SAVE_PROJECT);
		this.saveProjectMenuItem.addActionListener(new SaveProjectActionListener());
		this.fileMenu.add(this.saveProjectMenuItem);
		
		this.fileMenu.addSeparator();
		
		this.exitMenuItem = new JMenuItem(EXIT);
		this.exitMenuItem.addActionListener(new ExitActionListener());
		this.fileMenu.add(exitMenuItem);
		
		this.pipesMenu = new JMenu(PIPES);
		this.buildPipesMenuItems();
		this.add(this.pipesMenu);
		
		this.onSelectedProjectChange(UIController.getInstance().getSelectedProjectRepr());
		UIController.getInstance().onSelectedProjectChanged(new SelectedProjectChangedListener());
	}

	private void buildMenuItems(JMenu parentMenu, JSONObject jsonRoot) {
		for (String menuTitle : jsonRoot.keySet().stream().sorted().toArray(String[]::new)) {
			JMenu subMenu = new JMenu(menuTitle);
			JSONArray menuItemNames = jsonRoot.optJSONArray(menuTitle);
			for (int i = 0; i < menuItemNames.length(); i++) {
				String pipeName = menuItemNames.optString(i);
				if (pipeName != null) {
					PipeTypeEnum pipeType = Enum.valueOf(PipeTypeEnum.class, pipeName);
					JMenuItem menuItem = new JMenuItem(pipeType.getFriendlyName());
					NewPipeReprActionListener newPipeActionListener = new NewPipeReprActionListener(pipeType);
					menuItem.addActionListener(newPipeActionListener);
					this.newPipeMenuItems.add(menuItem);
					subMenu.add(menuItem);
				}
			}
			parentMenu.add(subMenu);
		}
	}

	private void buildPipesMenuItems() {
		try {
			InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("pipe_menu.json");
			if (stream != null) {
				JSONTokener tokener = new JSONTokener(stream);
				JSONObject jsonRoot = new JSONObject(tokener);
				this.buildMenuItems(this.pipesMenu, jsonRoot);
			}
		} catch (Exception e) {
            throw new RuntimeException("Could not create Pipe menu items", e);
        }
    }
	
	public void onSelectedProjectChange(ProjectRepr p) {
		boolean enabled = p != null;
		for (JMenuItem menuItem : this.newPipeMenuItems) {
			menuItem.setEnabled(enabled);
		}
		this.saveProjectMenuItem.setEnabled(enabled);
	}
	
	class NewProjectActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UIController.getInstance().createNewProjectRepr();
		}
	}

	class SaveProjectActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int result = MainMenuBar.this.projectFileChooser.showSaveDialog(MainMenuBar.this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File saveFile = MainMenuBar.this.projectFileChooser.getSelectedFile();
				UIController.getInstance().saveSelectedProjectRepr(saveFile);
			}
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

	class OpenProjectActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int result = MainMenuBar.this.projectFileChooser.showOpenDialog(MainMenuBar.this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File openFile = MainMenuBar.this.projectFileChooser.getSelectedFile();
				UIController.getInstance().openProjectRepr(openFile);
			}
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
