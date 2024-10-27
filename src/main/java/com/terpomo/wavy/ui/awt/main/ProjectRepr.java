package com.terpomo.wavy.ui.awt.main;

import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;

import com.terpomo.wavy.signals.ConstantWave;
import com.terpomo.wavy.signals.ConstantWavePipe;
import com.terpomo.wavy.ui.awt.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.awt.pipes.ConstantWavePipeRepr;

public class ProjectRepr extends Panel {

	private static final long serialVersionUID = -3136424835205807021L;

	protected List<AbstractPipeRepr> pipes;
	
	public ProjectRepr() {
		this.pipes = new ArrayList<AbstractPipeRepr>();
		ConstantWavePipeRepr pipeRepr = new ConstantWavePipeRepr(new ConstantWavePipe(new ConstantWave(44100, 400)), "Const Sine Wave");
		pipeRepr.setLocation(0, 0);
		this.pipes.add(pipeRepr);
		this.add(pipeRepr);
	}
	
}
