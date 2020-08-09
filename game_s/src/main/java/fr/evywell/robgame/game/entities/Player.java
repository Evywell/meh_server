package fr.evywell.robgame.game.entities;

import fr.evywell.common.maths.Vector3;
import fr.evywell.common.network.Packet;
import fr.evywell.common.timer.IntervalTimer;
import fr.evywell.robgame.game.movement.MovementInfo;
import fr.evywell.robgame.network.WorldSession;
import fr.evywell.robgame.opcode.Opcode;
import fr.evywell.robgame.world.WorldTime;
import static fr.evywell.robgame.game.movement.MovementType.*;

public class Player extends Unit {

    private byte cellStatus;
    private IntervalTimer timer;
    private WorldSession session;

    public Player(WorldSession session) {
        this.timer = new IntervalTimer(300);
        this.session = session;
    }

    public void sendGameObjectUpdate(GameObject go) {
        /*
        Packet pck = new Packet(Opcode.SMSG_NOTIFY_PLAYER_GAME_OBJECT_STATE);
        pck.putInt(go.uuid);
        pck.putInt(go.mapId);
        pck.putFloat(go.pos_x);
        pck.putFloat(go.pos_y);
        pck.putFloat(go.pos_z);
        pck.putFloat( go.orientation);
        session.send(pck);
         */
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

    @Override
    public int getType() {
        return GameObjectType.PLAYER;
    }


    public void moveToDirection(Vector3 direction) {
        direction.multiply(this.getSpeed());
        move(direction.multiply(WorldTime.getDeltaTime()));
    }

}
