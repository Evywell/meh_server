package fr.evywell.robclient;

import fr.evywell.robclient.display.Window;
import fr.evywell.robclient.display.components.HistoryComponent;
import fr.evywell.robclient.game.Game;
import fr.evywell.robclient.network.GameServerClient;
import fr.evywell.robclient.opcode.AuthOpcodeHandler;
import fr.evywell.robclient.opcode.GameOpcodeHandler;

import java.awt.*;

public class Application {

    private String ticket;
    private String token;
    private GameServerClient authClient;
    private GameServerClient gameClient;
    private Game game;
    private Window mainWindow;

    public Application() {
        authClient = new GameServerClient("127.0.0.1", 8888, new AuthOpcodeHandler(this), this);
        gameClient = new GameServerClient("127.0.0.1", 1337, new GameOpcodeHandler(this), this);
        try {
            authClient.connect();
            authClient.sendLoginPacket("admin", "admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launch() {
        Window w = createMainWindow();
        w.show();
    }

    public Game createGame() {
        Window w = createMainWindow();
        game = new Game(this, w.getPanel());
        return game;
    }

    public Window createMainWindow() {
        // On se connecte au serveur de jeu
        mainWindow = new Window("Ring Of Bones");
        return mainWindow;
    }

    public void shutdown() {
        authClient.close();
    }

    public GameServerClient getAuthClient() {
        return authClient;
    }

    public GameServerClient getGameClient() {
        return gameClient;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Window getMainWindow() {
        return this.mainWindow;
    }
}
