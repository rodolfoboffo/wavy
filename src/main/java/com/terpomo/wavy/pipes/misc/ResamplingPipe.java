package com.terpomo.wavy.pipes.misc;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.marshal.MarshalAttr;
import com.terpomo.wavy.marshal.MarshallingKeys;
import com.terpomo.wavy.signals.InterpolationMethod;

public class ResamplingPipe extends AbstractPipe {

    public static final int DEFAULT_INPUT_SAMPLE_RATE = Constants.DEFAULT_SAMPLE_RATE;
    public static final int DEFAULT_OUTPUT_SAMPLE_RATE = Constants.DEFAULT_SAMPLE_RATE;
    public static final InterpolationMethod DEFAULT_INTERPOLATION_METHOD = InterpolationMethod.NEAREST;

    private int inputSampleRate;
    private int outputSampleRate;
    private float resamplingFactor;
    private float offset;
    private float previousInputValue;
    private InterpolationMethod interpolationMethod;

    public ResamplingPipe() {
        this.inputSampleRate = DEFAULT_INPUT_SAMPLE_RATE;
        this.outputSampleRate = DEFAULT_OUTPUT_SAMPLE_RATE;
        this.interpolationMethod = DEFAULT_INTERPOLATION_METHOD;
        this.offset = 0.0f;
        this.previousInputValue = Float.NaN;
        this.recalculateResamplingFactor();
        this.buildInputPorts(1);
        this.buildOutputPorts(1);
    }

    @Override
    public synchronized void clearCache() {
        super.clearCache();
        this.offset = 0.0f;
        this.previousInputValue = Float.NaN;
    }

    @Override
    protected synchronized void doWork() {
        if (this.allInputPortsConnected() && this.allOutputPortsConnected()) {
            if (this.getInputPort().getBuffer().getSize() >= 2 && !this.getOutputPort().getLinkedPort().getBuffer().isFull()) {
                if (this.resamplingFactor <= 1.0f) {
                    if (this.offset == 0.0f) {
                        float value = this.getInputPort().getBuffer().pickOne();
                        this.offset = (this.offset + this.resamplingFactor) % 1.0f;
                        this.getOutputPort().getLinkedPort().getBuffer().put(value);
                    }
                    else {
                        float nextOffset = (this.offset + this.resamplingFactor);
                        if (nextOffset >= 1.0f) {
                            float value;
                            if (this.interpolationMethod.equals(InterpolationMethod.LINEAR)) {
                                // y-y0 = m(x-x0)
                                float yf = this.getInputPort().getBuffer().getValue(1);
                                float y0 = this.getInputPort().getBuffer().getValue(0);
                                float m = (yf-y0)/(nextOffset-this.offset);
                                value = m * (1.0f-this.offset) + y0;
                            }
                            else {
                                value = this.getInputPort().getBuffer().getValue((1.0f - this.offset) < 0.5f ? 0 : 1);
                            }
                            this.getInputPort().getBuffer().pickOne();
                            this.getOutputPort().getLinkedPort().getBuffer().put(value);
                            this.offset = nextOffset % 1.0f;
                        }
                        this.getInputPort().getBuffer().pickOne();
                        this.offset = nextOffset % 1.0f;
                    }
                }
            }
        }
    }

    @MarshalAttr(attrName=MarshallingKeys.KEY_INPUT_SAMPLE_RATE)
    public int getInputSampleRate() {
        return inputSampleRate;
    }

    @MarshalAttr(attrName=MarshallingKeys.KEY_INPUT_SAMPLE_RATE)
    public synchronized void setInputSampleRate(int inputSampleRate) {
        this.inputSampleRate = inputSampleRate;
        this.clearCache();
        this.recalculateResamplingFactor();
    }

    private void recalculateResamplingFactor() {
        this.resamplingFactor = (float)this.outputSampleRate / this.inputSampleRate;
    }

    @MarshalAttr(attrName=MarshallingKeys.KEY_OUTPUT_SAMPLE_RATE)
    public int getOutputSampleRate() {
        return outputSampleRate;
    }

    @MarshalAttr(attrName=MarshallingKeys.KEY_OUTPUT_SAMPLE_RATE)
    public synchronized void setOutputSampleRate(int outputSampleRate) {
        this.outputSampleRate = outputSampleRate;
        this.clearCache();
        this.recalculateResamplingFactor();
    }

    @MarshalAttr(attrName=MarshallingKeys.KEY_INTERPOLATION)
    public InterpolationMethod getInterpolationMethod() {
        return interpolationMethod;
    }

    @MarshalAttr(attrName=MarshallingKeys.KEY_INTERPOLATION)
    public void setInterpolationMethod(InterpolationMethod interpolationMethod) {
        this.interpolationMethod = interpolationMethod;
    }

    public InputPort getInputPort() {
        return this.getInputPorts().get(0);
    }

    public OutputPort getOutputPort() {
        return this.getOutputPorts().get(0);
    }
}
