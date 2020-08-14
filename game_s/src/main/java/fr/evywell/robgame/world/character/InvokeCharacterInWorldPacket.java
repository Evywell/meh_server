package fr.evywell.robgame.world.character;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.game.entities.Player;

import static fr.evywell.robgame.opcode.Opcode.SMSG_INVOKE_CHARACTER_IN_WORLD;

public class InvokeCharacterInWorldPacket extends Packet {

    public InvokeCharacterInWorldPacket(Player player) {
        super(SMSG_INVOKE_CHARACTER_IN_WORLD);
        player.putIntoPacket(this);
        // Ajout du temps d'exécution pour la synchro
        this.putLong(System.currentTimeMillis());
    }

}
