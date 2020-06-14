package fr.evywell.robgame.world.character;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.gameobject.Player;

import static fr.evywell.robgame.opcode.Opcode.SMSG_INVOKE_CHARACTER_IN_WORLD;

public class InvokeCharacterInWorldPacket extends Packet {

    public InvokeCharacterInWorldPacket(Player player) {
        setCmd(SMSG_INVOKE_CHARACTER_IN_WORLD);
        add("player", player.toMap());
    }

}
