package fr.evywell.robauth.network;

import com.jsoniter.JsonIterator;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.RequestFoundation;
import fr.evywell.common.network.Server;
import fr.evywell.common.network.Session;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class AuthServerHandler extends ChannelInboundHandlerAdapter {

    private final Server server;

    public AuthServerHandler(Server server) {
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String ipClient = ctx.channel().remoteAddress().toString();
        if (!this.server.canHandleRequest(ctx.channel().hashCode())) {
            Log.info("Protection anti DDOS activée pour " + ipClient);
            ctx.channel().close();
        }
        Log.info(String.format("Nouvelle requête provenant de %s", ipClient));
        // Construction de l'objet qui représente la requête
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
        if (!this.server.isAcceptNewConnection()) {
            ctx.channel().close();
            return;
        }
        Session s = new AuthSession(this.server, ctx.channel());
        this.server.addSession(s);
        this.server.getChannels().add(ctx.channel());
        Log.info("Nombre de sessions: " + this.server.getSessions().size());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        server.removeSession(ctx.channel().hashCode());
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Session s = this.server.getSession(ctx.channel().hashCode());
        s.kick();
        Log.error(cause.toString());
    }
}
