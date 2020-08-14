package fr.evywell.robclient.game.gameobject;

import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.Handler;
import fr.evywell.robclient.Application;

public class NotifyPlayerGameObjectStateHandler implements Handler {

    private Application app;

    public NotifyPlayerGameObjectStateHandler(Application app) {
        this.app = app;
    }

    @Override
    public void call(Session session, Object payload, Packet packet) {
        //NotifyPlayerGameObjectStateTram tram = (NotifyPlayerGameObjectStateTram) payload;
        //Log.info(String.format("Notification de la part de %s: %s;%s;%s", tram.uuid, tram.pos_x, tram.pos_y, tram.pos_z));
    }

    @Override
    public Object getPayload(Packet packet) {
        return null;
    }

}
