package fr.evywell.robgame.network;

import com.jsoniter.JsonIterator;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.RequestFoundation;
import fr.evywell.common.network.Server;
import fr.evywell.common.network.Session;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class GameServerHandler extends ChannelInboundHandlerAdapter {

    private final Server server;

    public GameServerHandler(Server server) {
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String ipClient = ctx.channel().remoteAddress().toString();
        Session s = this.server.getSession(ctx.channel().hashCode());
        try {
            RequestFoundation rq = RequestFoundation.fromString((String) msg);
            this.server.dispatch(rq.getCmd(), rq.getBody(), s);
        } catch (Exception e) {
            Log.error(String.format("Paquet mal formé. Déconnexion du client... %s, cause: %s", ipClient, e.getMessage()));
            s.kick();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Session session = new WorldSession(this.server, ctx.channel());
        this.server.addSession(session);
        this.server.getChannels().add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Session session = this.server.getSession(ctx.channel().hashCode());
        ((WorldServer) this.server).getWorld().removeSession((WorldSession) session);
    }
}
