package fr.evywell.robclient.game.character;

import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.EmptyTram;
import fr.evywell.common.opcode.Handler;
import fr.evywell.robclient.Application;

public class InvokeCharacterClientReadyHandler implements Handler {

    private Application app;

    public InvokeCharacterClientReadyHandler(Application app) {
        this.app = app;
    }

    @Override
    public void call(Session session, Object payload, Packet packet) {
        //app.getMainWindow().show();
    }

    @Override
    public Class getPayloadTemplate() {
        return EmptyTram.class;
    }
}
