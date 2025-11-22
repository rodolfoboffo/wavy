package com.terpomo.wavy.ui.pipes.sources;

import com.terpomo.wavy.pipes.sources.ConstantValuePipe;
import com.terpomo.wavy.signals.ConstantValue;
import com.terpomo.wavy.ui.pipes.AbstractSignalPipeRepr;
import com.terpomo.wavy.ui.pipes.PipePropertyRepr;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConstantValuePipeRepr extends AbstractSignalPipeRepr<ConstantValue, ConstantValuePipe> {

	private static final long serialVersionUID = 8289652143621889982L;
	public static final String VALUE = "Value";

	GridBagLayout contentLayout;

	public ConstantValuePipeRepr(ConstantValuePipe pipe) {
		super(pipe);
		this.contentLayout = new GridBagLayout();
		this.getContentPanel().setLayout(this.contentLayout);

		List<PipePropertyRepr<?>> pipeProperties = new ArrayList<>();

		PipePropertyRepr<Float> valueProperty = new PipePropertyRepr<>(Float.class, this, null, VALUE, this.getPipe()::getValue, null, this.getPipe()::setValue);
		pipeProperties.add(valueProperty);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		PipePropertyRepr signalOutputProperty = new PipePropertyRepr(null, this, null, OUTPUT_SIGNAL, null, pipe.getOutputPort());
		pipeProperties.add(signalOutputProperty);
		
		this.layoutPipePropertiesOnGrid(pipeProperties);
	}
}
