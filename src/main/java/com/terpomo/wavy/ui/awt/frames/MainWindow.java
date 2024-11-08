package com.terpomo.wavy.ui.awt.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.MenuBar;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.terpomo.wavy.ui.awt.UIController;
import com.terpomo.wavy.ui.awt.pipes.ProjectRepr;
import com.terpomo.wavy.ui.awt.util.DefaultWindowListener;

public class MainWindow extends Frame {

	private static final long serialVersionUID = 6740298435235868036L;
	private static final String WAVY = "Wavy";
	protected MenuBar mainMenu;
	protected LayoutManager layout;
	
	public MainWindow() {
		super(WAVY);
		this.setSize(800, 600);
		this.setBackground(Color.LIGHT_GRAY);
		this.addWindowListener(new DefaultWindowListener(this));
		
		this.mainMenu = new MainMenuBar();
		this.setMenuBar(mainMenu);
		
		this.layout = new BorderLayout();
		this.setLayout(this.layout);
		
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
