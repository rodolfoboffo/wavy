package com.terpomo.wavy.ui.pipes;

import com.terpomo.wavy.flow.IPipe;
import com.terpomo.wavy.ui.UIController;
import com.terpomo.wavy.ui.components.IWavyRepr;
import com.terpomo.wavy.ui.components.WavyPanel;
import com.terpomo.wavy.ui.util.PointOperation;
import com.terpomo.wavy.ui.util.WavyImages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public abstract class AbstractPipeRepr<T extends IPipe> extends WavyPanel implements IPipeRepr, IWavyRepr {

	private static final long serialVersionUID = -4460157397034830356L;
	public static final String SAMPLE_RATE = "Sample Rate";
	public static final String REMOVE_PIPE = "Remove pipe";
	public static final String CLEAR_CACHE = "Clear cache";
	private static final int DEFAULT_INSET_SIZE = 8;
	private static final String IS_BEING_MOVED = "IS_BEING_MOVED";


    private final T pipe;
	private String pipeName;
	private JLabel labelName;
	private JButton closeButton;
	private JButton clearButton;

	private final JPanel contentPanel;
	private final JPanel titlePanel;
	private final JPanel titleButtonsPanel;

	private boolean isBeingMoved;
	private Point originMousePosition;
	private Point originPipePosition;

	public AbstractPipeRepr(T pipe, String name) {
		super(DEFAULT_INSET_SIZE);
		this.pipe = pipe;
		this.pipeName = name;
		UIController.getInstance().addModelToReprMapEntry(pipe, this);
		this.contentPanel = new WavyPanel(4);
		this.titlePanel = new WavyPanel();
		this.titleButtonsPanel = new WavyPanel();
		this.buildBasePipeControls();
		this.addMouseListener(new PipeMouseListener());
		this.addMouseMotionListener(new PipeMouseMotionListener());
		this.addPropertyChangeListener(IS_BEING_MOVED, new IsBeingMovedListener());
	}

	@Override
	public void clearCache() {}

	public JPanel getContentPanel() {
		return contentPanel;
	}

	private void buildBasePipeControls() {
        LayoutManager mainLayout = new BorderLayout();
		this.setLayout(mainLayout);

		this.isBeingMoved = false;
		this.originMousePosition = null;
		this.originPipePosition = null;

        LayoutManager titleLayout = new BorderLayout();
		this.titlePanel.setLayout(titleLayout);
		this.add(BorderLayout.NORTH, this.titlePanel);

		LayoutManager titleButtonsLayout = new BoxLayout(this.titleButtonsPanel, BoxLayout.X_AXIS);
		this.titleButtonsPanel.setLayout(titleButtonsLayout);
		this.titlePanel.add(BorderLayout.EAST, this.titleButtonsPanel);

		this.labelName = new JLabel(this.pipeName);
		this.titlePanel.add(BorderLayout.WEST, this.labelName);

		this.clearButton = new JButton();
		this.clearButton.setToolTipText(CLEAR_CACHE);
		this.clearButton.setPreferredSize(new Dimension(14, 14));
		this.clearButton.addActionListener(new ClearCacheButtonActionListener());
		this.clearButton.setIcon(new ImageIcon(WavyImages.getInstance().SWEEP_IMAGE));
		this.titleButtonsPanel.add(this.clearButton);

		this.closeButton = new JButton();
		this.closeButton.setToolTipText(REMOVE_PIPE);
		this.closeButton.setPreferredSize(new Dimension(14, 14));
		this.closeButton.addActionListener(new CloseButtonActionListener());
		this.closeButton.setIcon(new ImageIcon(WavyImages.getInstance().CLOSE_IMAGE));
		this.titleButtonsPanel.add(this.closeButton);

		this.add(BorderLayout.CENTER, this.contentPanel);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(0, 0, this.getWidth()-1, this.getHeight()-1, 10, 10);
        g2d.dispose();
	}
	
	protected void layoutPipePropertiesOnGrid(List<PipePropertyRepr<?>> pipeProperties) {
		this.layoutPipePropertiesOnGrid(this.contentPanel, pipeProperties);
	}

	protected void layoutPipePropertiesOnGrid(Container container, List<PipePropertyRepr<?>> pipeProperties) {
		container.removeAll();
		for (int i = 0; i < pipeProperties.size(); i++) {
			@SuppressWarnings("rawtypes")
			PipePropertyRepr property = pipeProperties.get(i);
			property.layoutOnGrid(container, i);
		}
	}
	
	public T getPipe() {
		return pipe;
	}
	
	public void setName(String name) {
		this.pipeName = name;
		this.labelName.setText(this.pipeName);
	}
	
	public String getPipeName() {
		return pipeName;
	}
	
	public synchronized void setIsBeingMoved(boolean newValue) {
		if (newValue != this.isBeingMoved) {
			boolean oldValue = this.isBeingMoved;
			this.isBeingMoved = newValue;
			this.firePropertyChange(IS_BEING_MOVED, oldValue, newValue);
		}
	}
	
	protected void beginMovingPipe(Point mousePoint) {
		this.originMousePosition = (Point) mousePoint.clone();
		this.originPipePosition = (Point) this.getLocation().clone();
		AbstractPipeRepr.this.setIsBeingMoved(true);
	}
	
	protected void finishMovingPipe(Point mousePoint) {
		AbstractPipeRepr.this.setIsBeingMoved(false);
	}
	
	protected void onDrag(Point mousePoint) {
		if (this.isBeingMoved) {
			Point displace = PointOperation.sub(mousePoint, this.originMousePosition);
			Point destination = PointOperation.sum(displace, this.originPipePosition);
			this.setLocation(destination);
		}
	}
	
	protected void bringToTop() {
		this.getParent().setComponentZOrder(this, 0);
		this.revalidate();
		this.repaint();
	}

	@Override
	public void wavyDispose() {
		Container parent = this.getParent();
		if (parent != null) {
			parent.remove(this);
		}
	}

	class IsBeingMovedListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					if ((boolean)evt.getNewValue()) {
						AbstractPipeRepr.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					}
					else {
						AbstractPipeRepr.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				}
			});
		}
		
	}

	class PipeMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					AbstractPipeRepr.this.bringToTop();
				}
			});
		}

		@Override
		public void mousePressed(MouseEvent e) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					AbstractPipeRepr.this.bringToTop();
					AbstractPipeRepr.this.beginMovingPipe(e.getLocationOnScreen());
				}
			});
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					AbstractPipeRepr.this.finishMovingPipe(e.getLocationOnScreen());
				}
			});
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
		
	}
	
	class PipeMouseMotionListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					AbstractPipeRepr.this.onDrag(e.getLocationOnScreen());
				}
			});
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		}
	}

	class CloseButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			UIController.getInstance().removePipeRepr(AbstractPipeRepr.this);
		}
	}

	class ClearCacheButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UIController.getInstance().clearCache(AbstractPipeRepr.this);
		}
	}
}
