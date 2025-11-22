package com.terpomo.wavy.rtl;

import com.sun.jna.Pointer;

import java.util.Arrays;

public class RTLSDRDevice {


    private static final int DEFAULT_DIRECT_SAMPLING_FREQ_THRESHOLD = 28000000;
    private static final int DEFAULT_SAMPLE_RATE = 960000;
    private static final long DEFAULT_CENTER_FREQUENCY = 106300000;
    private Pointer pointer;
    private String name;
    private int[] tunerGains;
    private long centerFrequency;
    private int sampleRate;
    private boolean directSampling;
    private int tunerGain;
    private Thread asyncReadingThread;
    private boolean asyncReadingOn;
    private IRTLAPI.IReadAsyncCallback asyncReadingCb;

    public RTLSDRDevice(Pointer devicePointer) {
        this.pointer = devicePointer;
        this.name = "";
        this.tunerGains = new int[]{};
        this.centerFrequency = 0;
        this.sampleRate = 0;
        this.tunerGain = 0;
        this.asyncReadingThread = null;
        this.asyncReadingOn = false;
    }

    public void resetBuffer() {
        int result = IRTLAPI.INSTANCE.rtlsdr_reset_buffer(this.pointer);
        if (result != 0)
            throw new RuntimeException(String.format("Could not reset device buffer. Error code %d", result));
    }

    public void setTestMode(boolean on) {
        int result = IRTLAPI.INSTANCE.rtlsdr_set_testmode(this.pointer, on);
        if (result != 0)
            throw new RuntimeException(String.format("Could not set test mode %s. Error code %d", on, result));
    }

    public void setDataSamplingMode(RTLDataSamplingMode mode, int freqThreshold) {
        int result = IRTLAPI.INSTANCE.rtlsdr_set_ds_mode(this.pointer, mode.getValue(), freqThreshold);
        if (result != 0)
            throw new RuntimeException(String.format("Could not set data sampling mode %s. Error code %d", mode, result));
    }

    public void setDataSamplingMode(RTLDataSamplingMode mode) {
        this.setDataSamplingMode(mode, DEFAULT_DIRECT_SAMPLING_FREQ_THRESHOLD);
    }

    public void setCenterFrequency(long centerFrequency) {
        int result = IRTLAPI.INSTANCE.rtlsdr_set_center_freq64(this.pointer, centerFrequency);
        if (result != 0)
            throw new RuntimeException(String.format("Could not set center frequency %d. Error code %d", centerFrequency, result));
        this.centerFrequency = centerFrequency;
    }

    public void setDirectSamplingMode(boolean on) {
        int result = IRTLAPI.INSTANCE.rtlsdr_set_direct_sampling(this.pointer, on);
        if (result != 0)
            throw new RuntimeException(String.format("Could not set direct sampling mode %s. Error code %d", on, result));
        this.directSampling = on;
    }

    public boolean fetchDirectSampling() {
        long result = IRTLAPI.INSTANCE.rtlsdr_get_direct_sampling(this.pointer);
        this.directSampling = result == 1;
        return this.directSampling;
    }

    public boolean isDirectSampling() {
        return directSampling;
    }

    public long getCenterFrequency() {
        return centerFrequency;
    }

    public long fetchCenterFrequency() {
        long result = IRTLAPI.INSTANCE.rtlsdr_get_center_freq64(this.pointer);
        this.centerFrequency = result;
        return this.centerFrequency;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int fetchSampleRate() {
        int result = IRTLAPI.INSTANCE.rtlsdr_get_sample_rate(this.pointer);
        this.sampleRate = result;
        return this.sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        int result = IRTLAPI.INSTANCE.rtlsdr_set_sample_rate(this.pointer, sampleRate);
        if (result != 0)
            throw new RuntimeException(String.format("Could not set sample rate %d. Error code %d", sampleRate, result));
        this.sampleRate = sampleRate;
    }

    public int readSamples(byte[] buffer, int numSamples) {
        int[] readResult = new int[1];
        int result = IRTLAPI.INSTANCE.rtlsdr_read_sync(this.pointer, buffer, numSamples, readResult);
        if (result != 0)
            throw new RuntimeException(String.format("Could not read samples. Error code %d", result));
        return readResult[0];
    }

    public int[] fetchTunerGains() {
        int tunerGains[] = new int[1024];
        int numGains = IRTLAPI.INSTANCE.rtlsdr_get_tuner_gains(this.pointer, tunerGains);
        this.tunerGains = Arrays.copyOf(tunerGains, numGains);
        return this.tunerGains;
    }

    public void setTunerGain(int gain) {
        int result = IRTLAPI.INSTANCE.rtlsdr_set_tuner_gain(this.pointer, gain);
        if (result != 0)
            throw new RuntimeException(String.format("Could not set tuner gain %d. Error code %d", gain, result));
        this.tunerGain = gain;
    }

    public int fetchTunerGain() {
        int result = IRTLAPI.INSTANCE.rtlsdr_get_tuner_gain(this.pointer);
        this.tunerGain = result;
        return this.tunerGain;
    }

    synchronized public void startAsyncReading(IRTLAPI.IReadAsyncCallback cb) {
        if (this.asyncReadingOn) return;
        this.asyncReadingOn = true;
        this.asyncReadingCb = cb;
        this.asyncReadingThread = new AsyncReadingThread();
        this.asyncReadingThread.start();
    }

    class AsyncReadingThread extends Thread {
        @Override
        public void run() {
            super.run();
            IRTLAPI.INSTANCE.rtlsdr_read_async(RTLSDRDevice.this.pointer, RTLSDRDevice.this.asyncReadingCb, null, 0, 0);
            synchronized (RTLSDRDevice.this) {
                RTLSDRDevice.this.asyncReadingOn = false;
                RTLSDRDevice.this.asyncReadingCb = null;
            }
        }
    }

    public void cancelAsyncReading() {
        if (!this.asyncReadingOn) return;
        int result = IRTLAPI.INSTANCE.rtlsdr_cancel_async(this.pointer);
        if (result != 0)
            throw new RuntimeException("Could not stop async reading.");
        if (this.asyncReadingThread != null && this.asyncReadingThread.isAlive()) {
            try {
                this.asyncReadingThread.join();
                this.asyncReadingThread = null;
            } catch (InterruptedException e) {
                throw new RuntimeException("Could not join RTL device async reading thread.", e);
            }
        }
    }

    public void initialize() {
        try {
            this.resetBuffer();
            this.fetchTunerGains();
            if (this.tunerGains.length > 0)
                this.setTunerGain(this.tunerGains[this.tunerGains.length-1]);
            this.setTestMode(false);
            this.setCenterFrequency(DEFAULT_CENTER_FREQUENCY);
            this.setSampleRate(DEFAULT_SAMPLE_RATE);
            this.setDirectSamplingMode(false);
            this.setDataSamplingMode(RTLDataSamplingMode.RTLSDR_DS_IQ);
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize RTL device.", e);
        }
//        int[] gains = new int[100];
//        int nGains = IRTLAPI.INSTANCE.rtlsdr_get_tuner_gains(this.pointer, gains);
//        gains = Arrays.stream(gains).filter(g->g!=0).toArray();
//        System.out.println(String.format("Number of gains: %d, %s", nGains, Arrays.toString(gains)));
//        result = IRTLAPI.INSTANCE.rtlsdr_set_tuner_gain(this.pointer, gains[gains.length-1]);
//        System.out.println(String.format("Set gain result: %d", result));
//        int tunerGain = IRTLAPI.INSTANCE.rtlsdr_get_tuner_gain(this.pointer);
//        System.out.println(String.format("Tuner gain: %d", tunerGain));
//        result = IRTLAPI.INSTANCE.rtlsdr_set_sample_rate(this.pointer, 2048000);
//        System.out.println(String.format("Set sample rate result: %d", result));
//        int sampleRate = IRTLAPI.INSTANCE.rtlsdr_get_sample_rate(this.pointer);
//        System.out.println(String.format("Sample Rate: %d", sampleRate));
//        result = IRTLAPI.INSTANCE.rtlsdr_set_center_freq(this.pointer, 106300000);
//        System.out.println(String.format("Set frequency result: %d", result));
//        long centerFreq = IRTLAPI.INSTANCE.rtlsdr_get_center_freq64(this.pointer);
//        System.out.println(String.format("Center Frequency: %d", centerFreq));
//        result = IRTLAPI.INSTANCE.rtlsdr_set_direct_sampling(this.pointer, false);
//        System.out.println(String.format("Set Direct Sample result: %d", result));
//        int dsMode = IRTLAPI.INSTANCE.rtlsdr_get_direct_sampling(this.pointer);
//        System.out.println(String.format("Direct sample mode: %d", dsMode));
    }

    public void close() {
        int result = IRTLAPI.INSTANCE.rtlsdr_close(this.pointer);
        if (result != 0)
            throw new RuntimeException(String.format("Could not close device %s. Error code %d", this, result));
    }

    @Override
    public String toString() {
        return String.format("RTLDevice %s", this.name);
    }

    public boolean isAsyncReadingOn() {
        return asyncReadingOn;
    }
}
