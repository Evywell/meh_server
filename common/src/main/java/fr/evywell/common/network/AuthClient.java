package fr.evywell.common.network;

import fr.evywell.common.logger.Log;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.GenericFutureListener;

public class AuthClient extends Client {

    private String clientId;

    public AuthClient(String ip, int port) {
        super(ip, port);
    }

    public ClientResponseFuture sendClientIdRequest(Packet packet) {
        final ClientResponseFuture responseFuture = new ClientResponseFuture();

        channel.addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                channel.channel().pipeline().get(ClientChannelHandler.class).setResponseFuture(responseFuture);
                channel.channel().writeAndFlush(Unpooled.wrappedBuffer(packet.getBytes()));
            }
        });

        return responseFuture;
    }

    @Override
    public ChannelHandler getHandler() {
        return new ClientChannelInitializer();
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

}
