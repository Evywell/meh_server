package fr.evywell.robgame.game.movement;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.opcode.Opcode;

public class MovementUpdate extends Packet {

    public MovementUpdate(MovementInfo movement) {
        this.setCmd(Opcode.SMSG_MOVE_UPDATE);
        this.add("uuid", movement.uuid);
        this.add("posX", movement.position.x);
        this.add("posY", movement.position.y);
        this.add("posZ", movement.position.z);
        this.add("orientation", movement.orientation);
        this.add("flags", movement.flags);
    }

}
