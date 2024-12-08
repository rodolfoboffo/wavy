package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class FileReaderPipe extends AbstractPipe {

    private String inputFilePath;
    private File inputFile;
    private AudioInputStream audioStream;
    private boolean isFileOpen;

    public FileReaderPipe() {
        this.inputFilePath = "";
        this.isFileOpen = false;
    }

    @Override
    synchronized protected void doWork() {

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
        if (this.audioStream != null) {
            return this.audioStream.getFormat().getChannels();
        }
        return 0;
    }

    public float getSampleRate() {
        if (this.audioStream != null) {
            return this.audioStream.getFormat().getSampleRate();
        }
        return 0;
    }

    @Override
    public void initialize() {
        if (!this.inputFilePath.isEmpty()) {
            try {
                this.openInputFile(this.inputFilePath);
                this.buildOutputPipes(this.getNumOfChannels());
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
        if (this.audioStream != null) {
            try {
                this.audioStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                this.audioStream = null;
            }
        }
    }

    synchronized private void openInputFile(String filePath) throws UnsupportedAudioFileException, IOException {
            this.inputFile = new File(filePath);
            this.audioStream = AudioSystem.getAudioInputStream(inputFile);
    }

}
