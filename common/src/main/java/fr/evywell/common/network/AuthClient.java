package fr.evywell.common.network;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.GenericFutureListener;

public class AuthClient extends Client {

    private String clientId;

    public AuthClient(String ip, int port) {
        super(ip, port);
    }

    public ClientResponseFuture sendClientIdRequest(String clientId) {
        final ClientResponseFuture responseFuture = new ClientResponseFuture();

        channel.addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                channel.channel().pipeline().get(ClientChannelHandler.class).setResponseFuture(responseFuture);
                channel.channel().writeAndFlush(String.format("[1,{\"client_id\":\"%s\"}]" + System.lineSeparator(), clientId));
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
