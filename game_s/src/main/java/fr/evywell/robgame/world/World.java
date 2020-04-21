package fr.evywell.robgame.world;

import fr.evywell.common.config.Config;
import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.logger.Log;
import fr.evywell.robgame.config.RealmConfig;
import fr.evywell.robgame.database.AuthQuery;
import fr.evywell.robgame.game.gameobject.CreatureManager;
import fr.evywell.robgame.game.map.MapManager;
import fr.evywell.robgame.network.WorldSession;
import fr.evywell.robgame.opcode.OpcodeHandler;
import fr.evywell.robgame.time.Clock;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {

    private RealmConfig realm;
    private boolean running;
    private Map<Integer, Clock> timers;
    private WorldTime time;
    private List<WorldSession> sessions;

    private Database worldDb;
    private MapManager mapManager;
    private CreatureManager creatureManager;

    // TIMERS
    private static final int UPTIME_TIMER = 0;

    public World(Config config) {
        this.realm = (RealmConfig) config.get("realm");
        this.running = true;
        this.time = new WorldTime();
        this.sessions = new CopyOnWriteArrayList<>();

        // Définition des timers
        this.timers = new HashMap<>();
        this.timers.put(UPTIME_TIMER, new Clock(60000)); // 60 secondes
        this.mapManager = new MapManager();
        this.creatureManager = new CreatureManager();

        Container.setInstance(fr.evywell.robgame.Service.CREATURE_MANAGER, this.creatureManager);
    }

    public void start() {
        this.worldDb = (Database) Container.getInstance(Service.SERVICE_DATABASE_WORLD);
        Container.setInstance(fr.evywell.robgame.Service.OPCODE_HANDLER, new OpcodeHandler());
        this._initializeAndReset();
    }

    public void update(int delta) {
        // UPDATE uptime
        this.timers.get(UPTIME_TIMER).update(delta);
        if (this.timers.get(UPTIME_TIMER).passed()) {
            Log.info("Mise à jour de l'uptime");
            this._updateWorldUptime();
            this.timers.get(UPTIME_TIMER).reset();
        }

        // UPDATE des session
        for (WorldSession session : this.sessions) {
            session.update(delta);
        }

        // UPDATE maps
        this.mapManager.update(delta);
    }

    public void addSession(WorldSession session) {
        this.sessions.add(session);
    }

    public void removeSession(WorldSession session) {
        this.sessions.remove(session);
    }

    public boolean isStopped() {
        return !this.running;
    }

    public RealmConfig getRealm() {
        return realm;
    }

    private void _updateWorldUptime() {
        int uptime = this.time.getUptimeMinutes();
        /*
        PreparedStatement stmt = this.authDb.getPreparedStatement(AuthQuery.UPD_REALM_UPTIME);
        try {
            stmt.setInt(1, uptime);
            stmt.setInt(2, realm.realmId); // l'ID du realm
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
    }

    private void _initializeAndReset() {
        this.mapManager.initializeCachedMaps();
        this.creatureManager.initializeCachedCreatures();
        try {
            this.mapManager.createMap(1);
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
        // Reset de l'uptime du realm principal
        /*
        PreparedStatement stmt = this.authDb.getPreparedStatement(AuthQuery.UPD_REALM_UPTIME);
        try {
            stmt.setInt(1, 0);
            stmt.setInt(2, realm.realmId); // l'ID du realm
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

}
