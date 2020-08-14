package fr.evywell.robclient.game.character;

import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.Handler;
import fr.evywell.robclient.Application;
import fr.evywell.robclient.game.Game;
import fr.evywell.robclient.game.Player;

public class InvokeCharacterInWorldHandler implements Handler {

    private Application app;

    public InvokeCharacterInWorldHandler(Application app) {
        this.app = app;
    }

    @Override
    public void call(Session session, Object payload, Packet packet) {
        /*
        InvokeCharacterInWorldTram tram = (InvokeCharacterInWorldTram) payload;
        Player player = new Player();
        player.name = (String) tram.player.get("name");
        player.uuid = (String) tram.player.get("uuid");
        player.mapId = (int) tram.player.get("map_id");
        if (tram.player.get("pos_x") instanceof Integer) {
            player.pos_x = ((Integer) tram.player.get("pos_x")).floatValue();
        }
        if (tram.player.get("pos_y") instanceof Integer) {
            player.pos_y = ((Integer) tram.player.get("pos_y")).floatValue();
        }
        if (tram.player.get("pos_z") instanceof Integer) {
            player.pos_z = ((Integer) tram.player.get("pos_z")).floatValue();
        }
        Game game = app.createGame();
        game.setPlayer(player);
        game.buildWorld();
        */
    }

    @Override
    public Object getPayload(Packet packet) {
        return null;
    }
}
