package fr.evywell.robgame.game.entities;

import fr.evywell.common.network.Packet;

import static fr.evywell.robgame.opcode.Opcode.SMSG_GAME_OBJECT_INFO;

public class GameObjectInfoPacket extends Packet {

    public GameObjectInfoPacket(GameObject go) {
        super(SMSG_GAME_OBJECT_INFO);
        go.putIntoPacket(this);
    }

}
