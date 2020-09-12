package fr.evywell.robgame.network.spell;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.spell.Spell;

import static fr.evywell.robgame.opcode.Opcode.SMSG_CAST_FAILED;

public class OutOfRangePacket extends Packet {

    public OutOfRangePacket(Spell spell) {
        super(SMSG_CAST_FAILED);
        // TODO: Ajouter l'information visuel peut-être
    }

}
