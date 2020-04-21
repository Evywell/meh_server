package fr.evywell.robgame.time;

public class Clock {

    private long interval;
    private long current;

    public Clock(long interval) {
        this.interval = interval;
        this.current = 0;
    }

    public Clock() {
        this(0);
    }

    public void update(int delta) {
        this.current += delta;
    }

    public boolean passed() {
        return this.current >= this.interval;
    }

    public void reset() {
        this.current = 0;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

}
