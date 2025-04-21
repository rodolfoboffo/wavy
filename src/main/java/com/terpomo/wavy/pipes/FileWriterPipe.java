package com.terpomo.wavy.pipes;

import com.terpomo.wavy.Constants;
import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.Buffer;
import com.terpomo.wavy.flow.InputPort;
import com.terpomo.wavy.sound.LPCMEncoder;
import com.terpomo.wavy.util.ListUtils;
import com.terpomo.wavy.util.RandomAccessFileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FileWriterPipe extends AbstractPipe {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd hhmmss");
    private static final int WAVE_FORMAT_PCM = 0x1;
    private static final int MIN_SAMPLES_TO_WRITE = 1;
    private String outputDirectory;
    private RandomAccessFile outputFile;
    private int sampleRate;
    private int numOfChannels;
    private int bitsPerSample;
    private boolean bigEndian;
    private boolean signed;
    private LPCMEncoder encoder;
    private String outputFilePath;
    private List<Buffer> localBuffers;
    private boolean headerWritten;
    private int numberOfSamples;
    private int numberOfWrittenSamples;

    public FileWriterPipe() {
        this.outputDirectory = "";
        this.sampleRate = Constants.DEFAULT_SAMPLE_RATE;
        this.numOfChannels = 1;
        this.bitsPerSample = 8;
        this.bigEndian = false;
        this.signed = false;
        this.localBuffers = new ArrayList<>();
        this.numberOfSamples = 0;
        this.numberOfWrittenSamples = 0;
        this.headerWritten = false;
        this.buildInputPorts(this.getNumOfChannels());
    }

    @Override
    synchronized protected void doWork() {
        if (this.outputDirectory != null) {
            int samplesToRead = Integer.MAX_VALUE;
            for (Buffer b : this.localBuffers) {
                samplesToRead = Math.min(samplesToRead, b.getRemainingCapacity());
            }
            for (InputPort p : this.getInputPorts()) {
                samplesToRead = Math.min(samplesToRead, p.getBuffer().getSize());
            }
            if (samplesToRead > 0) {
                try {
                    for (int i = 0; i < this.numOfChannels; i++) {
                        this.localBuffers.get(i).putAll(this.getInputPorts().get(i).getBuffer().fetch(samplesToRead));
                    }
                    this.numberOfSamples += samplesToRead;
                    int numOfFrames = this.numberOfSamples - this.numberOfWrittenSamples;
                    if (numOfFrames >= MIN_SAMPLES_TO_WRITE) {
                        if (this.outputFile == null)
                            this.openFile();
                        if (!this.headerWritten)
                            this.writeHeader();
                        boolean oddNumOfSamplesWritten = this.numberOfWrittenSamples % 2 == 1;

                        this.numberOfWrittenSamples += numOfFrames;
                        byte[] data = this.encoder.encode(numOfFrames, this.localBuffers.stream().toArray(Buffer[]::new));
                        this.outputFile.seek(4);
                        RandomAccessFileUtils.writeIntReverse(4+24+8+this.numberOfWrittenSamples*this.bitsPerSample/8*this.numOfChannels, this.outputFile);
                        this.outputFile.seek(40);
                        RandomAccessFileUtils.writeIntReverse(this.numberOfWrittenSamples*this.bitsPerSample/8*this.numOfChannels, this.outputFile);
                        this.outputFile.seek(this.outputFile.length() - (oddNumOfSamplesWritten ? 1 : 0));
                        this.outputFile.write(data);
                        if (this.numberOfWrittenSamples % 2 == 1)
                            this.outputFile.writeByte(0);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void openFile() {
        this.headerWritten = false;
        try {
            this.outputFilePath = this.getOutputFilePath();
            this.outputFile = new RandomAccessFile(this.outputFilePath, "rw");
        } catch (FileNotFoundException e) {
            this.outputFilePath = null;
            this.outputFile = null;
            throw new RuntimeException(e);
        }
    }

    private void initializeEncoder() {
        this.encoder = new LPCMEncoder(this.getSampleRate(), this.getBitsPerSample(), this.isSigned(), this.isBigEndian(), this.getNumOfChannels());
    }

    synchronized private void initializeBuffers() throws NoSuchMethodException {
        this.localBuffers.clear();
        this.localBuffers = ListUtils.buildNewList(this.numOfChannels, Buffer.class, this.localBuffers, Buffer.class.getDeclaredConstructor(int.class, boolean.class), new Object[]{10240, false});
        for (InputPort p : this.getInputPorts()) {
            p.getBuffer().clear();
        }
        this.numberOfSamples = 0;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    private String getOutputFilePath() {
        return this.getOutputFilePath(Calendar.getInstance().getTime());
    }

    private String getOutputFilePath(Date time) {
        return Paths.get(this.outputDirectory, String.format("output-%s.wav", DATE_FORMATTER.format(time))).toString();
    }

    synchronized public void setOutputDirectory(File outputDir) {
        this.setOutputDirectory(outputDir.getAbsolutePath());
    }

    synchronized public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
        this.reset();
    }

    @Override
    public synchronized void initialize() {
        this.reset();
        super.initialize();
    }

    public int getNumOfChannels() {
        return this.numOfChannels;
    }

    synchronized public void setNumOfChannels(int numOfChannels) {
        this.numOfChannels = numOfChannels;
        this.buildInputPorts(this.numOfChannels);
        this.reset();
    }

    public int getBitsPerSample() {
        return this.bitsPerSample;
    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    synchronized public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        this.reset();
    }

    private boolean isBigEndian() {
        return this.bigEndian;
    }

    private boolean isSigned() {
        return this.signed;
    }

    synchronized private void reset() {
        this.outputFile = null;
        this.outputFilePath = null;
        try {
            this.initializeBuffers();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.initializeEncoder();
    }

    private void writeHeader() throws IOException {
        // RIFF header
        this.outputFile.write("RIFF".getBytes(StandardCharsets.US_ASCII));
        RandomAccessFileUtils.writeIntReverse(4+24+8+this.numberOfWrittenSamples*this.bitsPerSample/8*this.numOfChannels, this.outputFile);
        this.outputFile.write("WAVE".getBytes(StandardCharsets.US_ASCII));

        //fmt chunk
        this.outputFile.write("fmt ".getBytes(StandardCharsets.US_ASCII));
        RandomAccessFileUtils.writeIntReverse(16, this.outputFile);
        RandomAccessFileUtils.writeShortReverse((short) WAVE_FORMAT_PCM, this.outputFile);
        RandomAccessFileUtils.writeShortReverse((short) this.numOfChannels, this.outputFile);
        RandomAccessFileUtils.writeIntReverse(this.sampleRate, this.outputFile);
        RandomAccessFileUtils.writeIntReverse(this.sampleRate*this.numOfChannels*this.bitsPerSample/8, this.outputFile); //nAvgBytesPerSec
        RandomAccessFileUtils.writeShortReverse((short) (this.numOfChannels*this.bitsPerSample/8), this.outputFile); //nBlockAlign
        RandomAccessFileUtils.writeShortReverse((short) this.bitsPerSample, this.outputFile); //nBlockAlign

        //data chunk
        this.outputFile.write("data".getBytes(StandardCharsets.US_ASCII));
        RandomAccessFileUtils.writeIntReverse(this.numberOfWrittenSamples*this.bitsPerSample/8*this.numOfChannels, this.outputFile);
        this.headerWritten = true;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.outputFile != null) {
            try {
                this.outputFile.close();
                this.outputFile = null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
