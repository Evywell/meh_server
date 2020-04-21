package fr.evywell.common.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientChannelHandler extends ChannelInboundHandlerAdapter {

    private AuthClient client;
    private ClientResponseFuture responseFuture;

    public ClientChannelHandler(AuthClient client) {
        this.client = client;
    }

    public void setResponseFuture(ClientResponseFuture future) {
        this.responseFuture = future;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //ctx.write(String.format("[1,{\"client_id\":\"%s\"}]" + System.lineSeparator(), client.getClientId()));
        //ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        responseFuture.set((String) msg);
        //ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
