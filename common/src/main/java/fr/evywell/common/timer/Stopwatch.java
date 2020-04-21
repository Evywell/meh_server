package fr.evywell.common.timer;

public class Stopwatch {

    private long startTime;
    private long stopTime;

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
    }

    public long getDiffTime() {
        return this.stopTime - this.startTime;
    }

}
