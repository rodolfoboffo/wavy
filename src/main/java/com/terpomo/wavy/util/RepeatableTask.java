package com.terpomo.wavy.util;

import com.terpomo.wavy.WavyDisposable;

import java.util.Timer;
import java.util.TimerTask;

public class RepeatableTask implements WavyDisposable {

    private static final int GUI_UPDATER_INTERVAL = 100;
    private boolean isRunning;
    private final int interval;
    private final Runnable runnable;
    private final TimerTask timerTask;
    private final Timer timer;

    public RepeatableTask(Runnable runnable, int interval) {
        super();
        this.runnable = runnable;
        this.interval = interval;
        this.isRunning = true;
        this.timerTask = new TimerTask() {
            @Override
            public void run() {
                RepeatableTask.this.runnable.run();
            }
        };
        this.timer = new Timer();
    }

    public RepeatableTask(Runnable runnable) {
        this(runnable, GUI_UPDATER_INTERVAL);
    }

    @Override
    synchronized public void wavyDispose() {
        this.isRunning = false;
        this.timer.cancel();
    }

    public void start() {
        this.timer.schedule(this.timerTask, 0, this.interval);
    }
}
