package com.terpomo.wavy.pipes.modulation;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.math.ArcSineTable;
import com.terpomo.wavy.math.MathConstants;

public class FMDemodulationPipe extends AbstractPipe {

    private float amplitude;
    private int sampleRate;
    private Float rad, sin;
    private Float instantaneousFrequency;
    private final InputPort inputPort;
    private final OutputPort outputPort;
    private float modulationIndex = 75000f;
    private final ArcSineTable arcSineTable;
    private Float[] rads;
    private Float[] diffs;
    private final Float[] means;

    public FMDemodulationPipe() {
        this.arcSineTable = ArcSineTable.DEFAULT_ARC_SINE_TABLE;
        this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
        this.rad = null;
        this.sin = null;
        this.instantaneousFrequency = null;
        this.amplitude = 1.0f;
        this.means = new Float[16];
        this.buildInputPorts(1);
        this.buildOutputPorts(1);
        this.inputPort = this.getInputPorts().get(0);
        this.outputPort = this.getOutputPorts().get(0);
    }

    @Override
    synchronized protected void doWork() {
        if (this.allPortsConnected()) {
            if (!this.getOutputPort().getLinkedPort().getBuffer().isFull() && !this.getInputPort().getBuffer().isEmpty()) {
                float newSin = this.getInputPort().getBuffer().pickOne();
                if (this.sin == null) {
                    this.sin = newSin;
                }
                else {
                    boolean ascending = newSin > this.sin;
                    float newRad1 = (this.arcSineTable.getValue(sin) + MathConstants.PI2) % MathConstants.PI2;
                    float newRad2 = newRad1 <= MathConstants.PI ? MathConstants.PI - newRad1 : MathConstants.PI2 - (newRad1 - MathConstants.PI);
                    float newRad = (ascending && (newRad1 >= 0 && newRad1 <= MathConstants.HALF_PI) || ((newRad1 >= MathConstants.HALF_PI * 3 && newRad1 <= MathConstants.PI2))) ||
                            (!ascending && (newRad1 >= MathConstants.HALF_PI && newRad1 <= MathConstants.HALF_PI * 3)) ? newRad1 : newRad2;
                    this.sin = newSin;
                    if (this.rad == null) {
                        this.rad = newRad;
                    }
                    else {
                        float diff = (newRad - this.rad + MathConstants.PI2) % MathConstants.PI2;
                        diff = diff < MathConstants.PI ? diff : MathConstants.PI - (diff - MathConstants.PI);
                        this.instantaneousFrequency = (diff * this.sampleRate / MathConstants.PI2);
                        float modulatedValue = (this.modulationIndex - this.instantaneousFrequency) / this.modulationIndex;
                        this.rad = newRad;
                        this.putThroughPort(this.outputPort, modulatedValue);
                    }
                }


//                float sin = this.getInputPort().getBuffer().pickOne();
//                float newRad1 = (this.arcSineTable.getValue(sin) + MathConstants.PI2) % MathConstants.PI2;
//                float newRad2 = newRad1 <= MathConstants.PI ? MathConstants.PI - newRad1 : MathConstants.PI2 - (newRad1 - MathConstants.PI);
//                Float[] newRads = new Float[]{newRad1, newRad2};
//                if (this.rads == null) {
//                    this.rads = newRads;
//                }
//                else {
//                    int diffIndex = 0;
//                    Float[] newDiffs = new Float[4];
//                    for (int i = 0; i < 2; i++) {
//                        for (int j = 0; j < 2; j++) {
//                            float diff = (newRads[i] - this.rads[i] + MathConstants.PI2) % MathConstants.PI2;
//                            diff = diff < MathConstants.PI ? diff : MathConstants.PI - (diff - MathConstants.PI);
//                            newDiffs[diffIndex] = diff;
//                            diffIndex++;
//                        }
//                    }
//                    this.rads = newRads;
//                    if (this.diffs == null) {
//                        this.diffs = newDiffs;
//                    }
//                    else {
//                        int meanIndex = 0;
//                        for (int i = 0; i < 4; i++) {
//                            for (int j = 0; j < 4; j++) {
//                                this.means[meanIndex] = Math.abs(newDiffs[i] - this.diffs[j]);
//                                meanIndex++;
//                            }
//                        }
//                        this.diffs = newDiffs;
//                        float minorMean = Float.POSITIVE_INFINITY;
//                        int minorIndex = 0;
//                        for (int i = 0; i < 16; i++) {
//                            if (this.means[i] <= minorMean) {
//                                minorIndex = i;
//                                minorMean = this.means[i];
//                            }
//                        }
//                        int goodNewDiffIndex = minorIndex / 4;
//                        float goodDiff = newDiffs[goodNewDiffIndex];
//                        this.instantaneousFrequency = (goodDiff * this.sampleRate / MathConstants.PI2);
//                        float modulatedValue = (this.modulationIndex - this.instantaneousFrequency) / this.modulationIndex;
//                        this.putThroughPort(this.outputPort, modulatedValue);
//                    }
//                }




//                float sin = this.getInputPort().getBuffer().pickOne();
//                float newRad1 = (this.arcSineTable.getValue(sin) + MathConstants.PI2) % MathConstants.PI2;
//                float newRad2 = newRad1 <= MathConstants.PI ? MathConstants.PI - newRad1 : MathConstants.PI2 - (newRad1 - MathConstants.PI);
//                if (this.rad != null && this.instantaneousFrequency != null) {
//                    float predictedNewRad = (1.0f / this.sampleRate * MathConstants.PI2 * this.instantaneousFrequency + this.rad) % MathConstants.PI2;
//                    float diff1 = (predictedNewRad - newRad1 + MathConstants.PI2) % MathConstants.PI2;
//                    float diff2 = (predictedNewRad - newRad2 + MathConstants.PI2) % MathConstants.PI2;
//                    diff1 = diff1 < MathConstants.PI ? diff1 : MathConstants.PI - (diff1 - MathConstants.PI);
//                    diff2 = diff2 < MathConstants.PI ? diff2 : MathConstants.PI - (diff2 - MathConstants.PI);
//                    float minorDiff = diff1 <= diff2 ? newRad1 : newRad2;
//                    this.rad = minorDiff;
//    //                this.instantaneousFrequency = ((minorDiff * this.sampleRate / MathConstants.PI2) - this.instantaneousFrequency) * 0.1f + this.instantaneousFrequency;
//                    this.instantaneousFrequency = minorDiff * this.sampleRate / MathConstants.PI2;
//                    float modulatedValue = (this.modulationIndex - this.instantaneousFrequency) / this.modulationIndex;
//                    this.putThroughPort(this.outputPort, modulatedValue);
//                }
//                else if (this.rad != null) {
//                    float diff1 = (this.rad - newRad1 + MathConstants.PI2) % MathConstants.PI2;
//                    float diff2 = (this.rad - newRad2 + MathConstants.PI2) % MathConstants.PI2;
//                    diff1 = diff1 < MathConstants.PI ? diff1 : MathConstants.PI - (diff1 - MathConstants.PI);
//                    diff2 = diff2 < MathConstants.PI ? diff2 : MathConstants.PI - (diff2 - MathConstants.PI);
//                    float minorDiff = diff1 <= diff2 ? newRad1 : newRad2;
//                    this.rad = minorDiff;
//                    this.instantaneousFrequency = minorDiff * this.sampleRate / MathConstants.PI2;
//                }
//                else {
//                    this.rad = newRad1;
//                }
            }
        }
    }

    public InputPort getInputPort() {
        return inputPort;
    }

    public OutputPort getOutputPort() {
        return outputPort;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    synchronized public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public Float getModulationIndex() {
        return modulationIndex;
    }

    synchronized public void setModulationIndex(Float modulationIndex) {
        this.modulationIndex = modulationIndex;
    }

    public float getAmplitude() {
        return amplitude;
    }

    synchronized public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }
}
