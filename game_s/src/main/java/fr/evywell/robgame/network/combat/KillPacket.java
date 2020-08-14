package fr.evywell.robgame.network.combat;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.entities.Unit;
import fr.evywell.robgame.opcode.Opcode;

public class KillPacket extends Packet {

    public KillPacket(Unit killer, Unit victim) {
        super(Opcode.SMSG_COMBAT_KILL);
        putLong(killer.guid.getGuid());
        putLong(victim.guid.getGuid());
    }

}
