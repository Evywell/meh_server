package fr.evywell.robclient.network;

import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Client;
import fr.evywell.common.network.Packet;
import fr.evywell.common.opcode.OpcodeHandler;
import fr.evywell.robclient.game.character.Character;
import fr.evywell.robclient.opcode.AuthOpcodeHandler;
import fr.evywell.robclient.opcode.OpcodeGame;
import io.netty.channel.ChannelHandler;

import static fr.evywell.robclient.opcode.OpcodeGame.CMSG_INVOKE_CHARACTER_IN_WORLD;

public class GameServerClient extends Client {

    private OpcodeHandler handler;

    public GameServerClient(String ip, int port, OpcodeHandler handler) {
        super(ip, port);
        this.handler = handler;
    }

    public void sendLoginPacket(String login, String password) {
        Packet p = new Packet();
        p.setCmd(0);
        p.add("login", login);
        p.add("password", password);
        this.sendPacket(p);
    }

    public void sendLoginToGameServerPacket(String ticket, String token) {
        Packet p = new Packet();
        p.setCmd(0);
        p.add("token", token);
        p.add("ticket", ticket);
        this.sendPacket(p);
    }

    public void sendObtainTicket(String token) {
        Packet p = new Packet();
        p.setCmd(2);
        p.add("game_code", "ROB");
        p.add("token", token);
        this.sendPacket(p);
    }

    public void sendPacket(Packet packet) {
        this.channel.channel().writeAndFlush(packet.toJson() + System.lineSeparator());
    }

    @Override
    public ChannelHandler getHandler() {
        return new ClientChannelInitializer(handler);
    }

    public void sendInvokeCharacterInWorldPacket(Character character) {
        Packet p = new Packet();
        p.setCmd(CMSG_INVOKE_CHARACTER_IN_WORLD);
        p.add("character_uuid", character.uuid);
        this.sendPacket(p);
    }

    public void sendPlayerReady() {
        Packet p = new Packet();
        p.setCmd(OpcodeGame.CMSG_INVOKE_CHARACTER_CLIENT_READY);
        this.sendPacket(p);
    }
}
