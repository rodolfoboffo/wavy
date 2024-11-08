package com.terpomo.wavy.ui.awt.pipes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import com.terpomo.wavy.Disposable;
import com.terpomo.wavy.ui.awt.util.PointOperation;

public class DataFlowRepr extends Component implements Disposable {

	private static final long serialVersionUID = 872075419552267975L;
	
	private ProjectRepr parentProjectRepr;
	private PortRepr sourcePortRepr, destinationPortRepr;
	
	public DataFlowRepr(ProjectRepr parentProjectRepr, PortRepr sourcePortRepr, PortRepr destinationPortRepr) {
		this.parentProjectRepr = parentProjectRepr;
		this.sourcePortRepr = sourcePortRepr;
		this.destinationPortRepr = destinationPortRepr;
		this.sourcePortRepr.setDataFlowRepr(this);
		this.destinationPortRepr.setDataFlowRepr(this);
		this.setLocation(this.getLocationRelativeToProject());
	}
	
	public PortRepr getSourcePort() {
		return sourcePortRepr;
	}
	
	public PortRepr getDestinationPort() {
		return destinationPortRepr;
	}
	
	@Override
	public Dimension getSize() {
		Point sourceLocation = this.sourcePortRepr.getLocationOnScreen();
		Point destinationLocation = this.destinationPortRepr.getLocationOnScreen();
		Point distance = PointOperation.abs(PointOperation.sub(sourceLocation, destinationLocation));
		return new Dimension(distance.x, distance.y);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return this.getSize();
	}
	
	public Point getLocationRelativeToProject() {
		Point p = PointOperation.sub(this.sourcePortRepr.getLocationOnScreen(), this.parentProjectRepr.getLocationOnScreen());
		return p;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.BLACK);
		Point sourceLocation = this.sourcePortRepr.getLocationOnScreen();
		Point destinationLocation = this.destinationPortRepr.getLocationOnScreen();
		Point distance = PointOperation.sub(destinationLocation, sourceLocation);
		g2d.drawLine(0, 0, distance.x, distance.y);
		g2d.dispose();
	}

	@Override
	public void dispose() {
		this.sourcePortRepr.setDataFlowRepr(null);
		this.destinationPortRepr.setDataFlowRepr(null);
		this.getParent().remove(this);
	}

}
