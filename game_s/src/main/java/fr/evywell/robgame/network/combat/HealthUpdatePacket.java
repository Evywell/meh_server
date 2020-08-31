package fr.evywell.robgame.network.combat;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.entities.ObjectGuid;

import static fr.evywell.robgame.opcode.Opcode.SMSG_HEALTH_UPDATE;

public class HealthUpdatePacket extends Packet {

    public HealthUpdatePacket(ObjectGuid guid, int health) {
        super(SMSG_HEALTH_UPDATE);
        putLong(guid.getGuid());
        putInt(health);
    }

}
