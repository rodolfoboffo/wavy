package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.Buffer;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.rtl.RTLSDR;
import com.terpomo.wavy.rtl.RTLSDRDevice;

public class RTLPipe extends AbstractPipe {

    private static final int BUFFER_SIZE = Buffer.DEFAULT_DATASTREAM_BUFER_SIZE;
    private byte[] localBufffer;
    private final OutputPort iOutputPort, qOutputPort;
    private RTLSDRDevice device;

    public RTLPipe() {
        this.device = null;
        this.localBufffer = new byte[2 * BUFFER_SIZE];
        this.buildOutputPorts(2);
        this.iOutputPort = this.getOutputPorts().get(0);
        this.qOutputPort = this.getOutputPorts().get(1);
    }

    @Override
    synchronized protected void doWork() {
        if (this.allOutputPortsConnected()) {
            int samplesToRead = 2 * this.getMinOutputBufferRemainingCapacity();
            if (samplesToRead > 0) {
                int samplesRead = this.device.readSamples(this.localBufffer, samplesToRead);
                for (int i = 0; i < samplesRead/2; i++) {
                    this.putThroughPort(this.iOutputPort, (Byte.toUnsignedInt(this.localBufffer[2*i])-128) / 128.0f);
                    this.putThroughPort(this.qOutputPort, (Byte.toUnsignedInt(this.localBufffer[2*i+1])-128) / 128.0f);
                }
            }
        }
    }

    public int getSampleRate() {
        if (this.device != null)
            return this.device.getSampleRate();
        return 0;
    }

    public void setSampleRate(int sampleRate) {
        if (this.device != null)
            this.device.setSampleRate(sampleRate);
    }

    public long getCenterFrequency() {
        if (this.device != null)
            return this.device.getCenterFrequency();
        return 0;
    }

    synchronized public void setCenterFrequency(long centerFrequency) {
        if (this.device != null)
            this.device.setCenterFrequency(centerFrequency);
    }

    public String[] getDeviceNames() {
        return RTLSDR.listDeviceNames();
    }

    synchronized public void setDeviceIndex(Integer deviceIndex) {
        if (deviceIndex != null && deviceIndex >= 0) {
            try {
                if (this.device != null)
                    this.device.close();
                this.device = RTLSDR.openDevice(deviceIndex);
                this.device.initialize();
                int maxSampleRate = Math.max(BUFFER_SIZE, this.device.getSampleRate());
                this.localBufffer = new byte[maxSampleRate * 2 * 10];
            } catch (Exception e) {
                this.device = null;
                throw e;
            }
        }
    }

    public OutputPort getIOutputPort() {
        return iOutputPort;
    }

    public OutputPort getQOutputPort() {
        return qOutputPort;
    }
}
