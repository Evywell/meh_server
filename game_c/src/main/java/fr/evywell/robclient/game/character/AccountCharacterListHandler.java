package fr.evywell.robclient.game.character;

import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.Handler;
import fr.evywell.robclient.Application;

public class AccountCharacterListHandler implements Handler {

    private Application app;

    public AccountCharacterListHandler(Application app) {
        this.app = app;
    }

    @Override
    public void call(Session session, Object payload, Packet packet) {
        AccountCharacterListTram tram = (AccountCharacterListTram) payload;
        Log.info(String.format("Tu as (%d) personnages sur ce compte", tram.num_characters));
        for (Character c : tram.characters) {
            Log.info(String.format("uuid: %s, Name: %s", c.uuid, c.name));
        }
        if (!tram.characters.isEmpty()) {
            app.getGameClient().sendInvokeCharacterInWorldPacket(tram.characters.get(0));
        }
    }

    @Override
    public Class getPayloadTemplate() {
        return AccountCharacterListTram.class;
    }
}
