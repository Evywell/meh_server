package fr.evywell.robauth.network;


import fr.evywell.common.network.Server;
import fr.evywell.common.network.Session;
import io.netty.channel.Channel;


public class AuthSession extends Session {

    private String uuid;
    private String token;
    private boolean authenticated = false;
    private boolean gameServer = false;

    public AuthSession(Server server, Channel channel) {
        super(server, channel);
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public void setAuthenticated(boolean flag) {
        this.authenticated = flag;
    }

    public boolean isGameServer() {
        return this.isAuthenticated() && this.gameServer;
    }

    public void setGameServer(boolean flag) {
        this.gameServer = flag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
