package com.terpomo.wavy.ui.pipes.sources;

import com.terpomo.wavy.pipes.sources.ConstantWavePipe;
import com.terpomo.wavy.signals.ConstantWave;
import com.terpomo.wavy.ui.pipes.AbstractPipeRepr;
import com.terpomo.wavy.ui.pipes.AbstractSignalPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConstantWavePipeRepr extends AbstractSignalPipeRepr<ConstantWave, ConstantWavePipe> {

	private static final long serialVersionUID = 8289652143621889982L;
	public static final String FREQUENCY = "Frequency (Hz)";
	public static final String AMPLITUDE = "Amplitude";
	public static final String PHASE = "Phase (rad)";
	
	GridBagLayout contentLayout;

	public ConstantWavePipeRepr(ConstantWavePipe pipe) {
		super(pipe);
		this.contentLayout = new GridBagLayout();
		this.getContentPanel().setLayout(this.contentLayout);

		List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();
		PipePropertyRepr<Integer> sampleRateProperty = new PipePropertyRepr<Integer>(Integer.class, this, null, AbstractPipeRepr.SAMPLE_RATE, this.getPipe()::getSampleRate, null, this.getPipe()::setSampleRate);
		pipeProperties.add(sampleRateProperty);

		PipePropertyRepr<Float> frequencyProperty = new PipePropertyRepr<Float>(Float.class, this, null, FREQUENCY, this.getPipe()::getFrequency, null, this.getPipe()::setFrequency);
		pipeProperties.add(frequencyProperty);

		PipePropertyRepr<Float> amplitudeProperty = new PipePropertyRepr<Float>(Float.class, this, null, AMPLITUDE, this.getPipe()::getAmplitude, null, this.getPipe()::setAmplitude);
		pipeProperties.add(amplitudeProperty);

		PipePropertyRepr<String> phaseProperty = new PipePropertyRepr<String>(String.class, this, null, PHASE, this.getPipe()::getPhase, null, this.getPipe()::setPhase);
		pipeProperties.add(phaseProperty);
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		PipePropertyRepr signalOutputProperty = new PipePropertyRepr(null, this, null, OUTPUT_SIGNAL, null, pipe.getOutputPort());
		pipeProperties.add(signalOutputProperty);
		
		this.layoutPipePropertiesOnGrid(pipeProperties);
	}
}
