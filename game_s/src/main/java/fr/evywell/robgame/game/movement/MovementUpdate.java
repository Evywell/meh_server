package fr.evywell.robgame.game.movement;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.opcode.Opcode;

public class MovementUpdate extends Packet {

    public MovementUpdate(MovementInfo movement) {
        super(Opcode.SMSG_MOVE_UPDATE);
        this.putLong(movement.guid.getGuid());
        this.putFloat(movement.position.x);
        this.putFloat(movement.position.y);
        this.putFloat(movement.position.z);
        this.putFloat(movement.orientation);
        this.putInt(movement.flags);
    }

}
