package fr.evywell.common.timer;

public class IntervalTimer {

    private long current, interval;

    public IntervalTimer() {
        this.current = 0;
        this.interval = 0;
    }

    public IntervalTimer(long interval) {
        this();
        this.setInterval(interval);
    }

    public void reset() {
        if (this.current >= this.interval) {
            this.current %= this.interval;
        }
    }

    public void update(long delta) {
        this.current += delta;
        if (this.current < 0) {
            this.current = 0;
        }
    }

    public boolean passed() {
        return this.current >= this.interval;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
