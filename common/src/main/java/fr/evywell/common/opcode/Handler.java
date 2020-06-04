package fr.evywell.common.opcode;

import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;

public interface Handler {

    void call(Session session, Object payload, Packet packet);
    Class getPayloadTemplate();

}
