package fr.evywell.robgame.game.gameobject;

import fr.evywell.common.maths.Vector3;
import fr.evywell.common.network.Packet;
import fr.evywell.common.timer.IntervalTimer;
import fr.evywell.robgame.game.movement.MovementInfo;
import fr.evywell.robgame.game.movement.MovementPayload;
import fr.evywell.robgame.network.WorldSession;
import fr.evywell.robgame.opcode.Opcode;
import fr.evywell.robgame.world.WorldTime;
import static fr.evywell.robgame.game.movement.MovementType.*;

import java.util.HashMap;
import java.util.Map;

public class Player extends Unit {

    private byte cellStatus;
    private IntervalTimer timer;
    private WorldSession session;

    public String name;

    public Player(WorldSession session) {
        this.timer = new IntervalTimer(300);
        this.session = session;
    }

    public void sendGameObjectUpdate(GameObject go) {
        Packet pck = new Packet();
        pck.setCmd(Opcode.SMSG_NOTIFY_PLAYER_GAME_OBJECT_STATE);
        pck.add("uuid", go.uuid);
        pck.add("map_id", go.mapId);
        pck.add("pos_x", go.pos_x);
        pck.add("pos_y", go.pos_y);
        pck.add("pos_z", go.pos_z);
        pck.add("orientation", go.orientation);
        session.send(pck);
        //Log.info(String.format("Le joueur %s a reçu un message de la créature %s !", this.hashCode(), go.uuid));
    }

    public void sendPacket(Packet packet) {
        this.session.send(packet);
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        timer.update(delta);
        if (timer.passed()) {
            Packet pck = new Packet();
            pck.setCmd(4354354);
            //session.send(pck);
            timer.reset();
        }
    }

    public void validateMovement(MovementInfo movement) {
        // Forward et Backward
        if (movement.hasFlag(MOVEMENT_FORWARD) && movement.hasFlag(MOVEMENT_BACKWARD)) {
            // On supprime les deux flags
            movement.removeFlag(MOVEMENT_FORWARD | MOVEMENT_BACKWARD);
        }
        // Turn left et Turn right
        if (movement.hasFlag(MOVEMENT_TURN_LEFT) && movement.hasFlag(MOVEMENT_TURN_RIGHT)) {
            // On supprime les deux flags
            movement.removeFlag(MOVEMENT_TURN_LEFT | MOVEMENT_TURN_RIGHT);
        }
        // Strafe left et Strafe right
        if (movement.hasFlag(MOVEMENT_STRAFE_LEFT) && movement.hasFlag(MOVEMENT_STRAFE_RIGHT)) {
            // On supprime les deux flags
            movement.removeFlag(MOVEMENT_STRAFE_LEFT | MOVEMENT_STRAFE_RIGHT);
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> player = new HashMap<>();
        player.put("name", this.name);
        player.put("uuid", this.uuid);
        player.put("map_id", this.mapId);
        player.put("pos_x", this.pos_x);
        player.put("pos_y", this.pos_y);
        player.put("pos_z", this.pos_z);
        player.put("orientation", this.orientation);

        return player;
    }

    public void moveToDirection(Vector3 direction) {
        direction.multiply(this.getSpeed());
        move(direction.multiply(WorldTime.getDeltaTime()));
    }

}
