package com.terpomo.wavy.ui.util;

import com.terpomo.wavy.WavyDisposable;

public class GuiUpdaterWorker extends Thread implements WavyDisposable {

    private static final int GUI_UPDATER_INTERVAL = 1000;
    private boolean isRunning;
    private final int interval;
    private final Runnable runnable;

    public GuiUpdaterWorker(Runnable runnable, int interval) {
        super();
        this.runnable = runnable;
        this.interval = interval;
        this.isRunning = true;
    }

    public GuiUpdaterWorker(Runnable runnable) {
        this(runnable, GUI_UPDATER_INTERVAL);
    }

    @Override
    public void run() {
        super.run();
        while (this.isRunning) {
            this.runnable.run();
            try {
                Thread.sleep(this.interval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    synchronized public void wavyDispose() {
        this.isRunning = false;
    }
}
