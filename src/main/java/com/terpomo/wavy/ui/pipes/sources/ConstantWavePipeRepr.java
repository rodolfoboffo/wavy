package com.terpomo.wavy.ui.pipes.sources;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import com.terpomo.wavy.pipes.sources.ConstantWavePipe;
import com.terpomo.wavy.signals.ConstantWave;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.AbstractSignalPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

public class ConstantWavePipeRepr extends AbstractSignalPipeRepr<ConstantWave, ConstantWavePipe> {

	private static final long serialVersionUID = 8289652143621889982L;
	public static final String FREQUENCY = "Frequency (Hz)";
	public static final String AMPLITUDE = "Amplitude";
	
	GridBagLayout contentLayout;

	public ConstantWavePipeRepr(ConstantWavePipe pipe) {
		super(pipe);
		this.contentLayout = new GridBagLayout();
		this.getContentPanel().setLayout(this.contentLayout);

		@SuppressWarnings("rawtypes")
		List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();
		PipePropertyRepr<Integer> sampleRateProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, AbstractPipeRepr.SAMPLE_RATE, pipe.getSignal()::getSampleRate, null, this.getPipe()::setSampleRate);
		pipeProperties.add(sampleRateProperty);

		PipePropertyRepr<Float> frequencyProperty = new PipePropertyRepr<Float>(Float.class, this, null, FREQUENCY, pipe.getSignal()::getFrequency, null, this.getPipe()::setFrequency);
		pipeProperties.add(frequencyProperty);

		PipePropertyRepr<Float> amplitudeProperty = new PipePropertyRepr<Float>(Float.class, this, null, AMPLITUDE, pipe.getSignal()::getAmplitude, null, this.getPipe()::setAmplitude);
		pipeProperties.add(amplitudeProperty);
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		PipePropertyRepr signalOutputProperty = new PipePropertyRepr(null, this, null, OUTPUT_SIGNAL, null, pipe.getOutputPort());
		pipeProperties.add(signalOutputProperty);
		
		this.layoutPipePropertiesOnGrid(pipeProperties);
	}
}
