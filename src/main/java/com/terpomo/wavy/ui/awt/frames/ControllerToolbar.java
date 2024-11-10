package com.terpomo.wavy.ui.awt.frames;


import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.terpomo.wavy.ui.awt.UIController;

public class ControllerToolbar extends JToolBar {
	
	private static final long serialVersionUID = 3452803016099600418L;
	private static final String PAUSE = "Pause";
	private static final String PLAY = "Play";
	
	protected JButton playPauseButton;
	
	public ControllerToolbar() {
		this.playPauseButton = new JButton();
		this.playPauseButton.addActionListener(new PlayPauseButtonActionListener());
		this.updatePlayPauseButton();
		this.add(this.playPauseButton);
		
		UIController.getInstance().onPauseToggled(new PauseToggledChangeListener());
	}
	
	private void updatePlayPauseButton() {
		this.updatePlayPauseButton(UIController.getInstance().isPaused());
	}
	
	private void updatePlayPauseButton(boolean isPaused) {
		if (isPaused)
			this.playPauseButton.setText(PLAY);
		else
			this.playPauseButton.setText(PAUSE);
		this.playPauseButton.revalidate();
		this.playPauseButton.repaint();
	}
	
	class PauseToggledChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					ControllerToolbar.this.updatePlayPauseButton((boolean) evt.getNewValue());
				}
			});
		}
		
	}
	
	class PlayPauseButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UIController.getInstance().togglePause();
		}
		
	}
}
