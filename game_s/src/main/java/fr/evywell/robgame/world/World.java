package fr.evywell.robgame.world;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.ScalarUtil;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;
import fr.evywell.common.config.Config;
import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.logger.Log;
import fr.evywell.common.maths.Vector3;
import fr.evywell.robgame.config.RealmConfig;
import fr.evywell.robgame.game.gameobject.CreatureManager;
import fr.evywell.robgame.game.gameobject.Player;
import fr.evywell.robgame.game.map.MapManager;
import fr.evywell.robgame.network.WorldSession;
import fr.evywell.robgame.opcode.OpcodeHandler;
import fr.evywell.robgame.physics.DrawDebug;
import fr.evywell.robgame.physics.debug.display.Window;
import fr.evywell.robgame.time.Clock;
import fr.evywell.robgame.world.character.NewCharacterInWorldPacket;

import javax.vecmath.Vector3f;
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
    private Map<WorldSession, Player> playersQueue;

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
        this.playersQueue = new HashMap<>();

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

        // TODO: Collisions TEMP
        DrawDebug drawDebug = new DrawDebug(new Window("qskdqs"));
        DefaultCollisionConfiguration configuration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(configuration);
        BroadphaseInterface overlappingPairCache = new DbvtBroadphase();
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        DiscreteDynamicsWorld world = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, configuration);
        world.setDebugDrawer(drawDebug);
        world.setGravity(new Vector3f(0, -10f, 0));
        ObjectArrayList<CollisionShape> collisionShapes = new ObjectArrayList<>();
        CollisionShape colShape = new SphereShape(4f);
        collisionShapes.add(colShape);
        Transform startTransform = new Transform();
        startTransform.setIdentity();
        startTransform.origin.set(0, 0, 0);
        float mass = 0f;
        MotionState motionState = new DefaultMotionState(startTransform);
        RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(mass, motionState, colShape, new Vector3f(0, 0, 0));

        RigidBody body = new RigidBody(info);
        //world.addRigidBody(body);
        drawDebug.drawCircle(startTransform.origin, 4f);

        CollisionObject co = new CollisionObject();
        co.setCollisionShape(colShape);
        co.setWorldTransform(startTransform);
        world.addCollisionObject(co);

        for (int i = 5; i > -30; i--) {
            Vector3f from = new Vector3f(10, 10, 10);
            Vector3f to = new Vector3f(i, i, i);
            CollisionWorld.ClosestRayResultCallback callback = new CollisionWorld.ClosestRayResultCallback(from, to);
            world.rayTest(from, to, callback);
            drawDebug.drawLine(from, to, null);
            drawDebug.drawCircle(callback.hitPointWorld, 1);
            float length = callback.hitPointWorld.length();
            float fromLength = from.length();
            Log.debug("Direction " + ((length - fromLength) / callback.closestHitFraction));
            Log.debug(String.format("HitFraction %f for %d ", callback.closestHitFraction, i));
        }
        drawDebug.repaint();
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

    public void putPlayerInQueue(WorldSession session, Player player) {
        this.playersQueue.put(session, player);
    }

    public void spawnPlayerInWorld(WorldSession session) {
        if (this.playersQueue.containsKey(session)) {
            Player player = this.playersQueue.get(session);
            fr.evywell.robgame.game.map.Map map = mapManager.getMap(player.mapId);
            Log.info("Add player to map");
            map.addPlayerToMap(player);
            // On notifie tous les joueurs autour
            player.sendPacketToSet(new NewCharacterInWorldPacket(player), player);
        }
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
            e.printStackTrace();
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
