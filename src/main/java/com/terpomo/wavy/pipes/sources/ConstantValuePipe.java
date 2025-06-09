package com.terpomo.wavy.pipes.sources;

import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;
import com.terpomo.wavy.pipes.AbstractSignalSourcePipe;
import com.terpomo.wavy.signals.ConstantValue;

public class ConstantValuePipe extends AbstractSignalSourcePipe<ConstantValue> {

	public ConstantValuePipe() {
		super(new ConstantValue());
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_VALUE)
	public void setValue(float v) {
		this.signal.setConstantValue(v);
	}

	@MarshalAttr(attrName= MarshallingKeys.KEY_VALUE)
	public float getValue() {
		return this.signal.getConstantValue();
	}

}
