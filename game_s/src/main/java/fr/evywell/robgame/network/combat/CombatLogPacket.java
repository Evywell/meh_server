package fr.evywell.robgame.network.combat;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.entities.ObjectGuid;

import static fr.evywell.robgame.opcode.Opcode.SMSG_SPELL_DAMAGE;

public class CombatLogPacket extends Packet {

    public ObjectGuid source;
    public ObjectGuid target;
    public int damages;

    public CombatLogPacket() {
        super(SMSG_SPELL_DAMAGE);
    }

    @Override
    public byte[] getBytes() {
        putLong(source.getGuid());
        putLong(target.getGuid());
        putInt(damages);
        return super.getBytes();
    }
}
