package fr.evywell.robgame.game.movement;

import fr.evywell.common.maths.Vector3;
import fr.evywell.robgame.game.entities.ObjectGuid;

public class MovementPayload {

    public int guid;
    public float posX;
    public float posY;
    public float posZ;
    public float orientation;
    public int flags; // Les flags ici représentent le type de mouvement (forward, backward, flying, swimming, etc.)

    public MovementInfo toMovementInfo() {
        MovementInfo movement = new MovementInfo();
        movement.guid = ObjectGuid.createFromGuid(guid);
        movement.position = new Vector3(posX, posY, posZ);
        movement.orientation = orientation;
        movement.flags = flags;
        return movement;
    }

}
