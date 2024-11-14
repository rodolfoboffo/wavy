package com.terpomo.wavy.ui.pipes;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import com.terpomo.wavy.pipes.ConstantWavePipe;
import com.terpomo.wavy.signals.ConstantWave;

public class ConstantWavePipeRepr extends AbstractSignalPipeRepr<ConstantWave, ConstantWavePipe> {

	private static final long serialVersionUID = 8289652143621889982L;
	public static final String FREQUENCY = "Frequency";
	
	GridBagLayout contentLayout;
	protected ConstantWavePipe pipe;

	public ConstantWavePipeRepr(ConstantWavePipe pipe, String name) {
		super(pipe, name);
		this.pipe = (ConstantWavePipe) super.pipe;
		this.contentLayout = new GridBagLayout();
		this.getContentPanel().setLayout(this.contentLayout);

		@SuppressWarnings("rawtypes")
		List<PipePropertyRepr> pipeProperties = new ArrayList<>();
		PipePropertyRepr<Float> frequencyProperty = new PipePropertyRepr<Float>(Float.class, this, null, FREQUENCY, pipe.getSignal().getFrequency(), null, this.pipe::setFrequency);
		pipeProperties.add(frequencyProperty);
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		PipePropertyRepr signalOutputProperty = new PipePropertyRepr(null, this, null, OUTPUT_SIGNAL, null, pipe.getOutputPort());
		pipeProperties.add(signalOutputProperty);
		
		this.layoutPipePropertiesOnGrid(pipeProperties);
	}
}
