package fr.evywell.robgame.opcode;

import fr.evywell.common.network.Packet;
import fr.evywell.robgame.network.WorldSession;

public interface Handler {

    void call(WorldSession session, Object payload, Packet packet);
    Class getPayloadTemplate();

}
