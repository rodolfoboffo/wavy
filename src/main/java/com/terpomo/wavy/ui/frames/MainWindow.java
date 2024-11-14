package com.terpomo.wavy.ui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JToolBar;

import com.terpomo.wavy.WavyDisposable;
import com.terpomo.wavy.ui.UIController;
import com.terpomo.wavy.ui.util.DefaultWindowListener;

public class MainWindow extends JFrame implements WavyDisposable {

	private static final long serialVersionUID = 6740298435235868036L;
	private static final String WAVY = "Wavy";
	
	protected MainMenuBar mainMenu;
	protected LayoutManager mainLayout;
	
	protected JToolBar controllerToolbar;
	
	public MainWindow() {
		super(WAVY);
		this.setSize(800, 600);
		this.setBackground(Color.LIGHT_GRAY);
		this.addWindowListener(new DefaultWindowListener(this));
		
		this.mainMenu = new MainMenuBar();
		this.setJMenuBar(mainMenu);
		
		this.mainLayout = new BorderLayout();
		this.setLayout(this.mainLayout);
		
		this.controllerToolbar = new ControllerToolbar();
		this.add(BorderLayout.NORTH, this.controllerToolbar);
		
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

	@Override
	public void wavyDispose() {
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
