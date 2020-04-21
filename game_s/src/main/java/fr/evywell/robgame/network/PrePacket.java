package fr.evywell.robgame.network;

import com.jsoniter.JsonIterator;

/**
 * Structure d'un pré-paquet
 * _cmd: Opcode
 * iterator: Contenu JSON du paquet (payload)
 */
public class PrePacket {

    private final int cmd;
    private final JsonIterator iterator;

    public PrePacket(int _cmd, JsonIterator iterator) {
        cmd = _cmd;
        this.iterator = iterator;
    }

    public int getOpcode() {
        return cmd;
    }

    public JsonIterator getPayload() {
        return iterator;
    }
}
