package fr.evywell.robgame.network;

import com.jsoniter.JsonIterator;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.MalformedRequestException;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Server;
import fr.evywell.common.network.Session;
import fr.evywell.robgame.Main;
import fr.evywell.robgame.authentication.AuthTram;
import fr.evywell.robgame.authentication.AuthenticationChallenge;
import fr.evywell.robgame.world.World;
import fr.evywell.robgame.world.WorldTime;

import java.io.IOException;

import static fr.evywell.robgame.opcode.Opcode.WORLD_AUTHENTICATION_CHALLENGE;

public class WorldServer extends Server {

    private final World world;
    private String secret;

    public static final int WORLD_UPDATES = 50;

    public WorldServer(World world, int port) {
        super(port);
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public void loop() {
        long currentTime = 0;
        long previousTime = System.currentTimeMillis();

        while (!world.isStopped()) {
            currentTime = System.currentTimeMillis();
            int diff = (int) (currentTime - previousTime);

            WorldTime.setDeltaTime(diff);
            world.update(diff);

            previousTime = currentTime;
            long executionTimeDiff = System.currentTimeMillis() - currentTime;

            if (executionTimeDiff < WORLD_UPDATES) {
                try {
                    Thread.sleep(WORLD_UPDATES - executionTimeDiff);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void beforeStarting() {
        // Entête du serveur d'authentification
        System.out.println("\n" +
            "██████╗  ██████╗ ██████╗     ███████╗███████╗██████╗ ██╗   ██╗███████╗██████╗ \n" +
            "██╔══██╗██╔═══██╗██╔══██╗    ██╔════╝██╔════╝██╔══██╗██║   ██║██╔════╝██╔══██╗\n" +
            "██████╔╝██║   ██║██████╔╝    ███████╗█████╗  ██████╔╝██║   ██║█████╗  ██████╔╝\n" +
            "██╔══██╗██║   ██║██╔══██╗    ╚════██║██╔══╝  ██╔══██╗╚██╗ ██╔╝██╔══╝  ██╔══██╗\n" +
            "██║  ██║╚██████╔╝██████╔╝    ███████║███████╗██║  ██║ ╚████╔╝ ███████╗██║  ██║\n" +
            "╚═╝  ╚═╝ ╚═════╝ ╚═════╝     ╚══════╝╚══════╝╚═╝  ╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝\n" +
            "                                                                              \n");
    }

    @Override
    public void afterStopping() {
        Main.stopMysql();
    }

    @Override
    public void dispatch(int _cmd, Packet packet, Session session) throws MalformedRequestException {
        if (_cmd < 0) {
            throw new MalformedRequestException(String.format("La command %s n'existe pas", _cmd));
        }
        if (!((WorldSession) session).isAuthenticated() && _cmd != WORLD_AUTHENTICATION_CHALLENGE) {
            session.kick();
        }
        if (_cmd == WORLD_AUTHENTICATION_CHALLENGE) {
            AuthTram tram = new AuthTram();
            tram.ticket = packet.readString();
            tram.token = packet.readString();
            (new AuthenticationChallenge(tram, ((WorldSession) session), secret, world)).proceed();
        } else {
            ((WorldSession) session).pushInQueue(packet);
        }
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
