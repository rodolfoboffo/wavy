package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.math.FFT;
import com.terpomo.wavy.math.Utils;

public class BandPassFilterPipe extends AbstractPipe {

    private static final int MAX_RESOLUTION = 256;
    private int sampleRate;
    private int numOfChannels = 1;
    private float lowFrequency, highFrequency;
    private int resolution;
    private Float[] firFilter;

    public BandPassFilterPipe() {
        this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
        this.lowFrequency = 0;
        this.highFrequency = this.sampleRate / 2.0f;
        this.resolution = MAX_RESOLUTION;
        this.resetFirFilter();
        this.buildPorts();
    }

    private void resetFirFilter() {
        this.firFilter = this.getFir(this.lowFrequency, this.highFrequency, this.sampleRate, this.resolution);
    }

    private Float[] getFir(float lowFrequency, float highFrequency, int sampleRate, int resolution) {
        Float[] freqDomainFir = new Float[resolution];
        float step = 1.0f * sampleRate / resolution;
        float v = 0;
        for (int i = 0; i < resolution/2; i++) {
            v = (step*i <= highFrequency && step*i >= lowFrequency) ? 1.0f * resolution : 0.0f;
            freqDomainFir[i] = freqDomainFir[resolution-1-i] = v;
        }
        Float[] fir = FFT.ifft(freqDomainFir);
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

    synchronized private void buildPorts() {
        this.dispose();
        this.buildOutputPorts(this.numOfChannels);
        this.buildInputPorts(this.numOfChannels);
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
            float v = this.getInputPorts().get(i).getBuffer().pickOne();
            for (int j = 0; j < this.resolution; j++) {
                v += this.firFilter[j];
            }
            v -= this.resolution;
            this.getOutputPorts().get(i).getLinkedPort().getBuffer().put(v);
        }
    }
}
