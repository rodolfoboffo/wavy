package com.terpomo.wavy.ui.awt.pipes;

import com.terpomo.wavy.flow.IPipe;

public class OscilloscopePipeRepr extends AbstractPipeRepr {

	private static final long serialVersionUID = 7368297233394330359L;

	public OscilloscopePipeRepr(IPipe pipe, String name) {
		super(pipe, name);
//		this.createPipePropertiesForInputs(pipe);
//		this.layoutPipePropertiesOnGrid();
	}
	
//	protected void createPipePropertiesForInputs(OscilloscopePipe pipe) {
//		for (int i = 0; i < pipe.getInputPorts().size(); i++) {
//			IPort port = pipe.getInputPorts().get(i);
//			String propertyName = String.format("Channel %d", i+1);
//			@SuppressWarnings({ "rawtypes", "unchecked" })
//			PipePropertyRepr pipeProperty = new PipePropertyRepr(null, this, port, propertyName, null, null);
//			this.addPipeProperty(pipeProperty);
//		}
//	}

}
