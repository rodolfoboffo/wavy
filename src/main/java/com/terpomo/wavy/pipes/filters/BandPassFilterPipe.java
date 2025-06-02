package com.terpomo.wavy.pipes.filters;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.SignalBuffer;
import com.terpomo.wavy.math.FFT;
import com.terpomo.wavy.math.Utils;
import com.terpomo.wavy.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class BandPassFilterPipe extends AbstractPipe {

    public static final String PROPERTY_FIR_FILTER = "PROPERTY_FIR_FILTER";
    private static final int MAX_RESOLUTION = 256;
    private int sampleRate;
    private int numOfChannels = 1;
    private float lowFrequency, highFrequency;
    private int resolution;
    private List<SignalBuffer> buffers;
    private Float[] firFilter;

    public BandPassFilterPipe() {
        super();
        this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
        this.lowFrequency = 0;
        this.highFrequency = this.sampleRate / 2.0f;
        this.resolution = MAX_RESOLUTION;
        this.buffers = new ArrayList<>();
        this.resetFirFilter();
        this.buildPortsAndBuffers();
    }

    private void resetFirFilter() {
        this.firFilter = this.getFir(this.lowFrequency, this.highFrequency, this.sampleRate, this.resolution);
        this.firePropertyChange(PROPERTY_FIR_FILTER, null, this.firFilter);
    }

    private Float[] getFir(float lowFrequency, float highFrequency, int sampleRate, int resolution) {
        Float[] freqDomainFir = new Float[resolution];
        float step = 1.0f * sampleRate / resolution;
        float v = 0;
        for (int i = 0; i < resolution/2; i++) {
            v = (step*i <= highFrequency && step*i >= lowFrequency) ? 1.0f : 0.0f;
            freqDomainFir[i] = freqDomainFir[resolution-1-i] = v;
        }
        Float[] shiftedFir = FFT.ifft(freqDomainFir);
        Float[] fir = new Float[this.resolution];
        for (int i = 0; i < this.resolution; i++) {
            fir[i] = (shiftedFir[(i+this.resolution/2)%this.resolution]);
        }
        return fir;
    }

    public int getResolution() {
        return resolution;
    }

    synchronized public void setResolution(int resolution) {
        int r = Utils.getNearestPowerOfTwo(resolution);
        r = Math.min(r, MAX_RESOLUTION);
        this.resolution = r;
        this.resetFirFilter();
    }

    public float getLowFrequency() {
        return lowFrequency;
    }

    synchronized public void setLowFrequency(float lowFrequency) {
        this.lowFrequency = lowFrequency;
        this.resetFirFilter();
    }

    public float getHighFrequency() {
        return highFrequency;
    }

    synchronized public void setHighFrequency(float highFrequency) {
        this.highFrequency = highFrequency;
        this.resetFirFilter();
    }

    public int getSampleRate() {
        return sampleRate;
    }

    synchronized public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        this.resetFirFilter();
    }

    public int getNumOfChannels() {
        return numOfChannels;
    }

    synchronized private void buildPortsAndBuffers() {
        this.dispose();
        this.buildOutputPorts(this.numOfChannels);
        this.buildInputPorts(this.numOfChannels);
        try {
            this.buffers = ListUtils.buildNewList(this.numOfChannels, SignalBuffer.class, this.buffers, SignalBuffer.class.getDeclaredConstructor(int.class, boolean.class), new Object[]{MAX_RESOLUTION, true});
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    synchronized protected void doWork() {
        for (int i = 0; i < this.numOfChannels; i++) {
            if (this.getInputPorts().get(i).getBuffer().isEmpty()
                    || this.getOutputPorts().get(i).getLinkedPort() == null
                    || this.getOutputPorts().get(i).getLinkedPort().getBuffer().isFull())
                return;

        }
        for (int i = 0; i < this.numOfChannels; i++) {
            SignalBuffer b = this.buffers.get(i);
            float v = this.getInputPorts().get(i).getBuffer().pickOne();
            b.put(v);
            if (b.getSize() >= this.resolution) {
                v = 0;
                for (int j = this.resolution-1; j >= 0; j--) {
                    v += this.firFilter[j]*b.getValue(j);
                }
                this.putThroughPort(this.getOutputPorts().get(i), v);
            }
        }
    }

    public Float[] getFirFilter() {
        return firFilter.clone();
    }
}
