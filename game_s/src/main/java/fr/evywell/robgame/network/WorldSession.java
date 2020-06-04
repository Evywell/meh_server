package fr.evywell.robgame.network;

import fr.evywell.common.container.Container;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.Server;
import fr.evywell.common.network.Session;
import fr.evywell.robgame.Service;
import fr.evywell.common.opcode.Handler;
import fr.evywell.common.opcode.OpcodeHandler;
import fr.evywell.robgame.game.gameobject.Player;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldSession extends Session {

    private boolean authenticated;
    private List<PrePacket> queue;
    private OpcodeHandler handler;
    private String userUUID;
    private Player player;

    public WorldSession(Server server, Channel channel) {
        super(server, channel);
        this.authenticated = false;
        this.queue = Collections.synchronizedList(new ArrayList<>());
        this.handler = (OpcodeHandler) Container.getInstance(Service.OPCODE_HANDLER);
    }

    public void update(int delta) {
        // Récupération d'un packet dans la queue
        Packet pck = new Packet();
        while (this.isSocketOpen() && this.queue.iterator().hasNext()) {
            PrePacket prepck = this.queue.iterator().next();
            this.queue.remove(prepck);
            // Gestion de son Opcode (_cmd)
            try {
                Handler handler = this.handler.getHandler(prepck.getOpcode());
                Log.info("Traitement du packet avec l'OPCODE " + prepck.getOpcode() + " queue: " + this.queue.size());

                handler.call(this, prepck.getPayload().read(handler.getPayloadTemplate()), pck);
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

    public void pushInQueue(PrePacket pck) {
        this.queue.add(pck);
    }

    public String getUserUUID() {
        return this.userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
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
}
