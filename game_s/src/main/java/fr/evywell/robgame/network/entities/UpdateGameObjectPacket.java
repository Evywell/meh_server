package fr.evywell.robgame.network.entities;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.opcode.Opcode;

public class UpdateGameObjectPacket extends Packet {

    public static final byte TYPE_ADD = 0;
    public static final byte TYPE_REMOVE = 1;

    public UpdateGameObjectPacket(byte type) {
        super(Opcode.SMSG_UPDATE_OBJECT);
        putByte(type);
    }

}
