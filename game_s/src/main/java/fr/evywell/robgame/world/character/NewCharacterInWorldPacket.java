package fr.evywell.robgame.world.character;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.entities.Player;
import fr.evywell.robgame.opcode.Opcode;

public class NewCharacterInWorldPacket extends Packet {

    public NewCharacterInWorldPacket(Player player) {
        super(Opcode.SMSG_INVOKE_NEW_CHARACTER_IN_WORLD);
        player.putIntoPacket(this);
    }

}
