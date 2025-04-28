package com.terpomo.wavy.pipes.input;

import com.sun.jna.Pointer;
import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.GenericBuffer;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.rtl.IRTLAPI;
import com.terpomo.wavy.rtl.RTLSDR;
import com.terpomo.wavy.rtl.RTLSDRDevice;

import java.util.logging.Logger;

public class RTLPipe extends AbstractPipe {

    private static final Logger LOGGER = Logger.getLogger(RTLPipe.class.getName());
    private static final int BUFFER_SIZE = (int)1<<21;
    private final byte[] auxBuffer;
    private final GenericBuffer<Byte> localBufffer;
    private final OutputPort iOutputPort, qOutputPort;
    private RTLSDRDevice device;
    private final IRTLAPI.IReadAsyncCallback callback;

    public RTLPipe() {
        this.device = null;
        this.auxBuffer = new byte[2 * BUFFER_SIZE];
        this.localBufffer = new GenericBuffer<Byte>(Byte.class, Constants.MAX_SAMPLE_RATE, true);
        this.buildOutputPorts(2);
        this.iOutputPort = this.getOutputPorts().get(0);
        this.qOutputPort = this.getOutputPorts().get(1);

        this.callback = new IRTLAPI.IReadAsyncCallback() {
            @Override
            public void invoke(Pointer bufferPointer, int length, Pointer contextPointer) {
                synchronized (RTLPipe.this) {
                    bufferPointer.read(0, RTLPipe.this.auxBuffer, 0, length);

                    LOGGER.info(String.format("Read %d bytes from device buffer", length));
                    for (int i = 0; i < length; i++) {
                        RTLPipe.this.localBufffer.put(RTLPipe.this.auxBuffer[i]);
                    }
                }
            }
        };
    }

    @Override
    synchronized protected void doWork() {
        if (this.allOutputPortsConnected()) {
            if (!this.device.isAsyncReadingOn())
                this.device.startAsyncReading(this.callback);
            int samplesToRead = this.getMinOutputBufferRemainingCapacity();
            samplesToRead = Math.min(samplesToRead, this.localBufffer.getSize()/2);
            if (samplesToRead > 0) {
                for (int i = 0; i < samplesToRead; i++) {
                    this.putThroughPort(this.iOutputPort, (Byte.toUnsignedInt(this.localBufffer.pickOne())-128) / 128.0f);
                    this.putThroughPort(this.qOutputPort, (Byte.toUnsignedInt(this.localBufffer.pickOne())-128) / 128.0f);
                }
            }
        }
    }

    public int getSampleRate() {
        if (this.device != null)
            return this.device.getSampleRate();
        return 0;
    }

    synchronized public void setSampleRate(int sampleRate) {
        if (this.device != null) {
            this.device.cancelAsyncReading();
            this.device.setSampleRate(sampleRate);
        }
    }

    public long getCenterFrequency() {
        if (this.device != null)
            return this.device.getCenterFrequency();
        return 0;
    }

    synchronized public void setCenterFrequency(long centerFrequency) {
        if (this.device != null) {
            this.device.cancelAsyncReading();
            this.device.setCenterFrequency(centerFrequency);
        }
    }

    public String[] getDeviceNames() {
        return RTLSDR.listDeviceNames();
    }

    synchronized void stopDevice() {
        if (this.device != null) {
            this.device.cancelAsyncReading();
            this.device.close();
        }
    }

    synchronized void initializeDevice(int deviceIndex) {
        this.device = RTLSDR.openDevice(deviceIndex);
        this.device.initialize();
    }

    synchronized public void setDeviceIndex(Integer deviceIndex) {
        if (deviceIndex != null && deviceIndex >= 0) {
            try {
                this.stopDevice();
                this.initializeDevice(deviceIndex);
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

    @Override
    public synchronized void dispose() {
        this.stopDevice();
        super.dispose();
    }
}
