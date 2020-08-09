package fr.evywell.common.network;

import fr.evywell.common.logger.Log;
import io.netty.channel.Channel;
import io.netty.util.NetUtil;

import java.net.InetSocketAddress;

public class Session {

    private final Server server;
    private final Channel channel;

    public Session(Server server, Channel channel) {
        this.server = server;
        this.channel = channel;
    }

    public String remoteAddress() {
        return this.channel.remoteAddress().toString();
    }

    public String getIp() {
        InetSocketAddress address = (InetSocketAddress) this.channel.remoteAddress();
        String ip = NetUtil.bytesToIpAddress(address.getAddress().getAddress());

        return "::1".equals(ip) ? "127.0.0.1" : ip;
    }

    public void close() {
        this.channel.close();
    }

    public void send(Packet packet) {
        this.channel.writeAndFlush(packet.getBytes());
    }

    public Channel channel() {
        return this.channel;
    }

    public void kick() {
        Log.error(String.format("Kick de la session %s", this.remoteAddress()));
        // On envoie le message d'erreur
        // TODO: Pour le moment j'ai la flemme
        this.channel.writeAndFlush("Vous avez été déconnecté du serveur");
        // On retire la session du serveur
        this.server.removeSession(this.channel.hashCode());
        // On ferme la socket
        this.close();
    }

    public boolean isSocketOpen() {
        return this.channel.isOpen();
    }

    public Server getServer() {
        return this.server;
    }
}
