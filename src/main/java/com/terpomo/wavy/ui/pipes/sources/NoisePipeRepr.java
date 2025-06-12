package com.terpomo.wavy.ui.pipes.sources;

import com.terpomo.wavy.pipes.sources.NoisePipe;
import com.terpomo.wavy.signals.Noise;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.AbstractSignalPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NoisePipeRepr extends AbstractSignalPipeRepr<Noise, NoisePipe> {

	private static final long serialVersionUID = 8289652143621889982L;
	public static final String AMPLITUDE = "Amplitude";

	GridBagLayout contentLayout;

	public NoisePipeRepr(NoisePipe pipe) {
		super(pipe);
		this.contentLayout = new GridBagLayout();
		this.getContentPanel().setLayout(this.contentLayout);

		List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();

		PipePropertyRepr<Integer> sampleRateProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, AbstractPipeRepr.SAMPLE_RATE, this.getPipe()::getSampleRate, null, this.getPipe()::setSampleRate);
		pipeProperties.add(sampleRateProperty);

		PipePropertyRepr<Float> amplitudeProperty = new PipePropertyRepr<>(Float.class, this, null, AMPLITUDE, this.getPipe()::getAmplitude, null, this.getPipe()::setAmplitude);
		pipeProperties.add(amplitudeProperty);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		PipePropertyRepr signalOutputProperty = new PipePropertyRepr(null, this, null, OUTPUT_SIGNAL, null, pipe.getOutputPort());
		pipeProperties.add(signalOutputProperty);
		
		this.layoutPipePropertiesOnGrid(pipeProperties);
	}
}
