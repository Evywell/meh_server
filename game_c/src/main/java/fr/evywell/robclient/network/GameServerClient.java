package fr.evywell.robclient.network;

import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Client;
import fr.evywell.common.network.Packet;
import fr.evywell.common.opcode.OpcodeHandler;
import fr.evywell.robclient.Application;
import fr.evywell.robclient.game.character.Character;
import fr.evywell.robclient.opcode.AuthOpcodeHandler;
import fr.evywell.robclient.opcode.OpcodeGame;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;

import static fr.evywell.robclient.opcode.OpcodeGame.CMSG_INVOKE_CHARACTER_IN_WORLD;

public class GameServerClient extends Client {

    private OpcodeHandler handler;
    private Application application;

    public GameServerClient(String ip, int port, OpcodeHandler handler, Application app) {
        super(ip, port);
        this.handler = handler;
        application = app;
    }

    public void sendLoginPacket(String login, String password) {
        Packet p = new Packet(0);
        p.putString(login);
        p.putString(password);
        this.sendPacket(p);
    }

    public void sendLoginToGameServerPacket(String ticket, String token) {
        Packet p = new Packet(0);
        p.putString(token);
        p.putString(ticket);
        this.sendPacket(p);
    }

    public void sendObtainTicket(String token) {
        Packet p = new Packet(2);
        p.putString("ROB");
        p.putString(token);
        this.sendPacket(p);
    }

    public void sendPacket(Packet packet) {
        this.channel.channel().writeAndFlush(Unpooled.wrappedBuffer(packet.getBytes()));
    }

    @Override
    public ChannelHandler getHandler() {
        return new ClientChannelInitializer(handler, application.getMainWindow());
    }

    public void sendInvokeCharacterInWorldPacket(Character character) {
        Packet p = new Packet(CMSG_INVOKE_CHARACTER_IN_WORLD);
        p.putString(character.uuid);
        this.sendPacket(p);
    }

    public void sendPlayerReady() {
        Packet p = new Packet(OpcodeGame.CMSG_INVOKE_CHARACTER_CLIENT_READY);
        this.sendPacket(p);
    }
}
