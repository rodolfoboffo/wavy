package com.terpomo.wavy.pipes;

import com.terpomo.wavy.flow.AbstractPipe;

import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.Observable;

public class FileReaderPipe extends AbstractPipe {

    private String inputFilePath;
    private File inputFile;
    private AudioInputStream audioInputStream;

    @Override
    protected void doWork() {

    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    synchronized public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    synchronized private void openInputFile() {

    }
}
