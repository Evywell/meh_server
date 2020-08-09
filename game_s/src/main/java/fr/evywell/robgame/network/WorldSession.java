package fr.evywell.robgame.network;

import fr.evywell.common.container.Container;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Server;
import fr.evywell.common.network.Session;
import fr.evywell.robgame.Service;
import fr.evywell.common.opcode.Handler;
import fr.evywell.common.opcode.OpcodeHandler;
import fr.evywell.robgame.game.entities.Player;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldSession extends Session {

    private boolean authenticated;
    private List<Packet> queue;
    private OpcodeHandler handler;
    private String userGuid;
    private Player player;
    private long latency = 0;

    public WorldSession(Server server, Channel channel) {
        super(server, channel);
        this.authenticated = false;
        this.queue = Collections.synchronizedList(new ArrayList<>());
        this.handler = (OpcodeHandler) Container.getInstance(Service.OPCODE_HANDLER);
    }

    public void update(int delta) {
        // Récupération d'un packet dans la queue
        while (this.isSocketOpen() && this.queue.iterator().hasNext()) {
            Packet pck = this.queue.iterator().next();
            this.queue.remove(pck);
            // Gestion de son Opcode (_cmd)
            try {
                Handler handler = this.handler.getHandler(pck.getOpcode());
                Log.info("Traitement du packet avec l'OPCODE " + pck.getOpcode() + " queue: " + this.queue.size());

                handler.call(this, handler.getPayload(pck), pck);
            } catch (Exception e) {
                // Il n'y a pas d'handler lié à cet OPCODE
                e.printStackTrace();
            }
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean flag) {
        this.authenticated = flag;
    }

    public void pushInQueue(Packet pck) {
        this.queue.add(pck);
    }

    public String getUserGuid() {
        return this.userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    @Override
    public void kick() {
        super.kick();
        this.authenticated = false;
        ((WorldServer) this.getServer()).getWorld().removeSession(this);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setLatency(long clientLatency) {
        if (clientLatency > 0) {
            latency = clientLatency;
        } else {
            latency = 0;
        }
    }

    public long getLatency() {
        return latency;
    }
}
