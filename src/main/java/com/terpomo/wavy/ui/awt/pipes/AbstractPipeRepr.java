package com.terpomo.wavy.ui.awt.pipes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;

import com.terpomo.wavy.flow.IPipe;

public abstract class AbstractPipeRepr extends Panel {

	private static final long serialVersionUID = -4460157397034830356L;
	private static final String ABS_PIPE_NAME = "Abstract Pipe";
	private static final Font NAME_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	protected IPipe pipe;
	protected LayoutManager layout;
	protected String name = ABS_PIPE_NAME;
	protected Label labelName;
	
	public AbstractPipeRepr(IPipe pipe, String name) {
		this.pipe = pipe;
		
		this.layout = new BorderLayout();
		this.setBackground(Color.lightGray);
		
		this.name = name;
		this.labelName = new Label(this.name);
		this.labelName.setFont(NAME_FONT);
		this.add(BorderLayout.NORTH, this.labelName);
	}
}
