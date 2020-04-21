package fr.evywell.robgame.world;

public class WorldTime {

    private long startedAt;

    public WorldTime() {
        this.startedAt = System.currentTimeMillis();
    }

    public long getUptimeMS() {
        return System.currentTimeMillis() - this.startedAt;
    }

    public int getUptimeMinutes() {
        long uptime = this.getUptimeMS();
        return Math.round(uptime / 60000);
    }

}
