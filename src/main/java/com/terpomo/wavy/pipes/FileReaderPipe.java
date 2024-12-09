package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;
import com.terpomo.wavy.flow.AbstractPort;
import com.terpomo.wavy.flow.Buffer;
import com.terpomo.wavy.flow.OutputPort;
import com.terpomo.wavy.sound.LPCMCodec;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileReaderPipe extends AbstractPipe {

    private String inputFilePath;
    private File inputFile;
    private AudioInputStream audioStream;
    private boolean isFileOpen;
    private LPCMCodec codec;
    private AudioFormat audioFormat;
    private Buffer[] outputBuffers;

    public FileReaderPipe() {
        this.inputFilePath = "";
        this.isFileOpen = false;
    }

    @Override
    synchronized protected void doWork() {
        try {
            Float[][] values = null;
            int numOfFrames = this.audioStream.available() / this.getNumOfChannels() * 8 / this.getBitsPerSample();
            for (int i = 0; i < this.getNumOfChannels(); i++) {
                Buffer b = this.outputBuffers[i];
                numOfFrames = Math.min(b.getRemainingCapacity(), numOfFrames);
            }
            int numBytesToRead = numOfFrames * this.getBitsPerSample() / 8 * this.getNumOfChannels();
            byte[] readBytes = new byte[numBytesToRead];
            int numBytesRead = this.audioStream.read(readBytes);
            if (numBytesRead > 0) {
                values = this.codec.decode(readBytes, numBytesRead);
            }
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    this.outputBuffers[i].putAll(values[i]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read audio file.", e);
        }
    }

    public boolean isFileOpen() {
        return isFileOpen;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    synchronized public void setInputFile(File inputFile) {
        this.setInputFilePath(inputFile.getAbsolutePath());
    }

    synchronized public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        this.initialize();
    }

    public int getNumOfChannels() {
        if (this.audioFormat != null) {
            return this.audioFormat.getChannels();
        }
        return 0;
    }

    public int getBitsPerSample() {
        if (this.audioFormat != null) {
            return this.audioFormat.getSampleSizeInBits();
        }
        return 0;
    }

    public int getSampleRate() {
        if (this.audioFormat != null) {
            return (int) this.audioFormat.getSampleRate();
        }
        return 0;
    }

    private boolean isBigEndian() {
        if (this.audioFormat != null) {
            return this.audioFormat.isBigEndian();
        }
        return false;
    }

    private AudioFormat.Encoding getEncoding() {
        if (this.audioFormat != null) {
            return this.audioFormat.getEncoding();
        }
        return null;
    }

    private boolean isSigned() {
        return this.getEncoding() == AudioFormat.Encoding.PCM_SIGNED;
    }

    private Buffer[] getOutputBuffers() {
        List<OutputPort> outputPorts = this.getOutputPorts();
        Buffer[] buffers = new Buffer[outputPorts.size()];
        for (int i = 0; i < outputPorts.size(); i++) {
            buffers[i] = outputPorts.get(i).getBuffer();
        }
        return buffers;
    }

    @Override
    public void initialize() {
        if (!this.inputFilePath.isEmpty()) {
            try {
                this.openInputFile(this.inputFilePath);
                this.buildOutputPipes(this.getNumOfChannels());
                AudioFormat.Encoding encoding = this.getEncoding();
                if (encoding != AudioFormat.Encoding.PCM_UNSIGNED && encoding != AudioFormat.Encoding.PCM_SIGNED) {
                    throw new RuntimeException(String.format("Encoding not expected. %s", encoding.toString()));
                }
                this.outputBuffers = this.getOutputBuffers();
                this.codec = new LPCMCodec(this.getSampleRate(), this.getBitsPerSample(), this.isSigned(), this.isBigEndian(), this.outputBuffers);
                this.isFileOpen = true;
                super.initialize();
            } catch (UnsupportedAudioFileException | IOException e) {
                this.dispose();
                this.buildOutputPipes(0);
                throw new RuntimeException(e);
            }
        }
        super.initialize();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.isFileOpen = false;
        this.inputFile = null;
        this.codec = null;
        this.audioFormat = null;
        this.outputBuffers = null;
        if (this.audioStream != null) {
            try {
                this.audioStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close audio stream.", e);
            } finally {
                this.audioStream = null;
            }
        }
    }

    synchronized private void openInputFile(String filePath) throws UnsupportedAudioFileException, IOException {
            this.inputFile = new File(filePath);
            this.audioStream = AudioSystem.getAudioInputStream(inputFile);
            this.audioFormat = this.audioStream.getFormat();
    }

}
