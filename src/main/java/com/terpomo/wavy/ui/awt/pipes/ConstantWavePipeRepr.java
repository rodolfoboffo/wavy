package com.terpomo.wavy.ui.awt.pipes;

import java.awt.GridBagLayout;

import com.terpomo.wavy.signals.ConstantWave;
import com.terpomo.wavy.signals.ConstantWavePipe;

public class ConstantWavePipeRepr extends AbstractSignalPipeRepr<ConstantWave, ConstantWavePipe> {

	private static final long serialVersionUID = 8289652143621889982L;
	public static final String FREQUENCY = "Frequency";
	
	GridBagLayout contentLayout;
	protected ConstantWavePipe pipe;

	public ConstantWavePipeRepr(ConstantWavePipe pipe, String name) {
		super(pipe, name);
		this.pipe = (ConstantWavePipe) super.pipe;
		this.contentLayout = new GridBagLayout();
		this.contentPanel.setLayout(this.contentLayout);
		
		PipePropertyRepr<Float> frequencyProperty = new PipePropertyRepr<Float>(Float.class, this, null, FREQUENCY, pipe.getSignal().getFrequency(), null, this.pipe::setFrequency);
		this.addPipeProperty(frequencyProperty);
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		PipePropertyRepr signalOutputProperty = new PipePropertyRepr(null, this, null, OUTPUT_SIGNAL, null, pipe.getOutputPort());
		this.addPipeProperty(signalOutputProperty);
		
		this.layoutPipePropertiesOnGrid();
	}
}
