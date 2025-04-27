package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.SignalBuffer;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.sound.AudioUtils;
import com.terpomo.wavy.sound.LPCMDecoder;

import javax.sound.sampled.*;


public class MicPipe extends AbstractPipe {

    private static final int MAX_BUFFER_SIZE = 10240;
    private static final int SAMPLE_RATE = 44100;
    private static final int BITS_PER_SAMPLE = 8;
    private static final int CHANNELS = 1;
    private static final boolean SIGNED = false;
    private static final boolean BIG_ENDIAN = false;
    private final AudioFormat audioFormat;
    private final byte[] localBuffer;
    OutputPort outputPort;
    private final LPCMDecoder decoder;
    private Mixer.Info mixer;
    private TargetDataLine line;

    public MicPipe() {
        this.audioFormat = new AudioFormat(SAMPLE_RATE, BITS_PER_SAMPLE, CHANNELS, SIGNED, BIG_ENDIAN);
        this.decoder = new LPCMDecoder(SAMPLE_RATE, BITS_PER_SAMPLE, SIGNED, BIG_ENDIAN, CHANNELS);
        this.outputPort = new OutputPort(this);
        this.getOutputPorts().add(this.outputPort);
        this.localBuffer = new byte[MAX_BUFFER_SIZE];
    }

    @Override
    protected synchronized void doWork() {
        if (this.line != null && this.line.isOpen() && this.outputPort.getLinkedPort() != null) {
            int bytesAvailable = this.line.available();
            int framesAvailable = bytesAvailable / CHANNELS;
            SignalBuffer buffer = this.outputPort.getLinkedPort().getBuffer();
            int bufferSpace = buffer.getRemainingCapacity();
            int framesToRead = Math.min(bufferSpace, framesAvailable);
            if (framesToRead > 0) {
                this.line.read(this.localBuffer, 0, framesToRead);
                Float[][] samples = this.decoder.decode(this.localBuffer, framesToRead * CHANNELS);
                this.putAllThroughPort(this.outputPort, samples[0]);
            }
        }
    }

    @Override
    public synchronized void initialize() {
        this.startLine();
        super.initialize();
    }

    public synchronized void startLine() {
        if (this.mixer != null && AudioSystem.getMixer(this.mixer).isLineSupported(new DataLine.Info(TargetDataLine.class, audioFormat))) {
            try {
                this.line = AudioSystem.getTargetDataLine(this.audioFormat, this.mixer);
                this.line.open();
                this.line.start();
            } catch (LineUnavailableException e1) {
                throw new RuntimeException("Could not open audio line.", e1);
            }
        }
        else {
            throw new RuntimeException("Audio Format not supported.");
        }
    }

    synchronized private void closeLine() {
        if (this.line != null) {
            this.line.flush();
            if (this.line.isActive()) {
                this.line.stop();
            }
            if (this.line.isOpen()) {
                this.line.close();
            }
            this.line = null;
        }
    }

    @Override
    synchronized public void dispose() {
        super.dispose();
        this.closeLine();
    }

    synchronized public void setMixer(String name) {
        Mixer.Info mixer = AudioUtils.getMixerInfoByName(name);
        this.mixer = mixer;
        this.dispose();
    }

    public String[] getMixerInfos() {
        return AudioUtils.getMixerInfos(TargetDataLine.class, this.audioFormat);
    }

}
