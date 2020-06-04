package fr.evywell.robgame.opcode;

import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.Handler;
import fr.evywell.robgame.network.WorldSession;

public abstract class AbstractHandler implements Handler {

    @Override
    public void call(Session session, Object payload, Packet packet) {
        call((WorldSession) session, payload, packet);
    }

    public abstract void call(WorldSession session, Object payload, Packet packet);
}
