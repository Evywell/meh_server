package fr.evywell.robclient.game;

import fr.evywell.robclient.Application;
import fr.evywell.robclient.display.Window;
import fr.evywell.robclient.network.GameServerClient;

import javax.swing.*;

public class Game {

    private Player player;
    private Application app;
    private GameServerClient client;
    private JComponent baseComponent;
    private Map map;

    public Game(Application app, JComponent baseComponent) {
        this.app = app;
        this.client = app.getGameClient();
        this.baseComponent = baseComponent;
    }

    public void buildWorld() {
        // On construit le monde à partir des infos du Player
        Map map = new Map(this, player, baseComponent, 50, 50);
        map.build();
        app.getMainWindow().show();

        // On demande au serveur de nous instancier
        client.sendPlayerReady();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
