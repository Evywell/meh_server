package fr.evywell.robgame.game.gameobject;

import fr.evywell.common.network.Packet;

import static fr.evywell.robgame.opcode.Opcode.SMSG_GAME_OBJECT_INFO;

public class GameObjectInfoPacket extends Packet {

    public GameObjectInfoPacket(GameObject go) {
        setCmd(SMSG_GAME_OBJECT_INFO);
        setData(go.toMap());
    }

}
