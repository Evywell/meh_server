package fr.evywell.robgame.game.movement;

import com.jsoniter.annotation.JsonProperty;
import fr.evywell.common.maths.Vector3;

public class MovementPayload {

    @JsonProperty(required = true)
    public String uuid;
    @JsonProperty(required = true)
    public float posX;
    @JsonProperty(required = true)
    public float posY;
    @JsonProperty(required = true)
    public float posZ;
    @JsonProperty(required = true)
    public float orientation;
    @JsonProperty(required = true)
    public int flags; // Les flags ici représentent le type de mouvement (forward, backward, flying, swimming, etc.)

    public MovementInfo toMovementInfo() {
        MovementInfo movement = new MovementInfo();
        movement.uuid = uuid;
        movement.position = new Vector3(posX, posY, posZ);
        movement.orientation = orientation;
        movement.flags = flags;
        return movement;
    }

}
