package fr.evywell.robgame.world;

public class WorldTime {

    private static int delta = 0;
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

    /**
     * @return Le nombre de ms écoulée depuis le dernier update
     */
    public static int getDeltaTime() {
        return delta;
    }

    public static void setDeltaTime(int delta) {
        WorldTime.delta = delta;
    }

}
