package com.terpomo.wavy.ui.frames;

import com.terpomo.wavy.IWavyDisposable;
import com.terpomo.wavy.ui.UIController;
import com.terpomo.wavy.ui.util.DefaultWindowListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainWindow extends JFrame implements IWavyDisposable {

	private static final long serialVersionUID = 6740298435235868036L;
	private static final String WAVY = "Wavy";
	
	private MainMenuBar mainMenu;
	private LayoutManager mainLayout;

	private JToolBar controllerToolbar;
	private JTabbedPane projectsTabbedPane;
	
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

		this.projectsTabbedPane = new JTabbedPane();
		this.add(BorderLayout.CENTER, this.projectsTabbedPane);
		this.projectsTabbedPane.addChangeListener(new ProjectTabSelectionChanged());

		UIController.getInstance().onSelectedProjectChanged(new SelectedProjectChangedListener());
		UIController.getInstance().onAddedProjectChanged(new AddedProjectChangedListener());
	}

	private void addProject(ProjectRepr newProject) {
		this.projectsTabbedPane.add(newProject);
	}

	public void setProject(ProjectRepr p) {
		if (p != null) {
			this.projectsTabbedPane.setSelectedComponent(p);
		}
		else {
			if (this.projectsTabbedPane.getComponentCount() > 0)
				this.projectsTabbedPane.setSelectedIndex(0);
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

	class AddedProjectChangedListener implements  PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			MainWindow.this.addProject((ProjectRepr)evt.getNewValue());
		}
	}

	class ProjectTabSelectionChanged implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			JTabbedPane projectsTabbedPane = (JTabbedPane) e.getSource();
			ProjectRepr selectedProject = (ProjectRepr) projectsTabbedPane.getSelectedComponent();
			if (UIController.getInstance().getSelectedProjectRepr() != selectedProject)
				UIController.getInstance().setSelectedProjectRepr(selectedProject);
		}
	}
}
