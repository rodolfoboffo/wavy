package com.terpomo.wavy.ui.awt.frames;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.MenuBar;
import java.awt.Panel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.terpomo.wavy.ui.awt.UIController;
import com.terpomo.wavy.ui.awt.pipes.ProjectRepr;
import com.terpomo.wavy.ui.awt.util.DefaultWindowListener;

public class MainWindow extends Frame {

	private static final long serialVersionUID = 6740298435235868036L;
	private static final String WAVY = "Wavy";
	protected MenuBar mainMenu;
	protected LayoutManager mainLayout;
	
	protected Panel toolBarPanel;
	protected LayoutManager toolBarLayout;
	protected Button b1;
	
	public MainWindow() {
		super(WAVY);
		this.setSize(800, 600);
		this.setBackground(Color.LIGHT_GRAY);
		this.addWindowListener(new DefaultWindowListener(this));
		
		this.mainMenu = new MainMenuBar();
		this.setMenuBar(mainMenu);
		
		this.mainLayout = new BorderLayout();
		this.setLayout(this.mainLayout);
		
		this.toolBarPanel = new Panel();
		this.toolBarLayout = new FlowLayout(FlowLayout.LEFT);
		this.add(BorderLayout.NORTH, this.toolBarPanel);
		this.b1 = new Button("Button b1");
		this.toolBarPanel.add(this.b1);
		
		UIController.getInstance().onSelectedProjectChanged(new SelectedProjectChangedListener());
	}
	
	public void setProject(ProjectRepr p) {
		if (p != null) {
			this.add(BorderLayout.CENTER, p);
		}
		else {
			this.removeAll();
		}
		this.revalidate();
		this.repaint();
	}
	
	public void showApp() {
		this.setVisible(true);
	}
	
	class SelectedProjectChangedListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			EventQueue.invokeLater(() -> {
				MainWindow.this.setProject((ProjectRepr) evt.getNewValue());
			});
		}
		
	}
}
