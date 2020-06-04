package fr.evywell.robgame.world.character;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.gameobject.Player;
import fr.evywell.robgame.opcode.Opcode;

public class NewCharacterInWorldPacket extends Packet {

    public NewCharacterInWorldPacket(Player player) {
        this.setCmd(Opcode.SMSG_INVOKE_NEW_CHARACTER_IN_WORLD);
        this.add("player", player.toMap());
    }

}
