package fr.evywell.robgame.game.entities;

import fr.evywell.common.maths.Vector3;
import fr.evywell.common.network.Packet;
import fr.evywell.common.timer.IntervalTimer;
import fr.evywell.robgame.game.movement.MovementInfo;
import fr.evywell.robgame.network.WorldSession;
import fr.evywell.robgame.network.entities.AddGameObjectPacket;
import fr.evywell.robgame.network.entities.RemoveGameObjectPacket;
import fr.evywell.robgame.opcode.Opcode;
import fr.evywell.robgame.world.WorldTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static fr.evywell.robgame.game.movement.MovementType.*;

public class Player extends Unit {

    private byte cellStatus;
    private IntervalTimer cleanTimer;
    private WorldSession session;
    private List<ObjectGuid> clientGuids; // Liste des objets gérés par le client

    public Player(WorldSession session) {
        this.cleanTimer = new IntervalTimer(5000);
        this.session = session;
        this.clientGuids = new CopyOnWriteArrayList<>();
    }

    public void sendPacket(Packet packet) {
        this.session.send(packet);
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        cleanTimer.update(delta);
        if (cleanTimer.passed()) {
            this.cleanClientVisibilityObjects();
            cleanTimer.reset();
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

    public boolean clientHasObject(GameObject go) {
        return go == this || (this.clientGuids.contains(go.guid));
    }

    public void updateVisibilityOf(GameObject go) {
        if (clientHasObject(go)) {
            if (!canSeeOrDetect(go)) {
                clientGuids.remove(go.guid);
                this.sendPacket(new RemoveGameObjectPacket(go));
            }
        } else {
            if (canSeeOrDetect(go)) {
                clientGuids.add(go.guid);
                this.sendPacket(new AddGameObjectPacket(go));
            }
        }
    }

    private void cleanClientVisibilityObjects() {
        if (clientGuids.isEmpty())
            return;

        for (ObjectGuid guid : clientGuids) {
            GameObject go = getMap().findGameObject(guid);
            if (go == null) {
                clientGuids.remove(guid);
                this.sendPacket(new RemoveGameObjectPacket(guid));
                return;
            }
            updateVisibilityOf(go);
        }
    }
}
