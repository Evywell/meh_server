package fr.evywell.robclient.auth;

import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.Handler;
import fr.evywell.robclient.Application;

public class TicketHandler implements Handler {

    private Application app;

    public TicketHandler(Application app) {
        this.app = app;
    }

    @Override
    public void call(Session session, Object payload, Packet packet) {
        app.setTicket(((TicketTram)payload).ticket);
        try {
            app.getGameClient().connect();
            app.getGameClient().sendLoginToGameServerPacket(app.getTicket(), app.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class getPayloadTemplate() {
        return TicketTram.class;
    }
}
