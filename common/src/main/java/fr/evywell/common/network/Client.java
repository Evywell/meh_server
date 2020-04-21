package fr.evywell.common.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

public abstract class Client {

    private final String ip;
    private final int port;
    protected ChannelFuture channel;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect() throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b
            .group(workerGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(getHandler());

        channel = b.connect(ip, port);

        if (!channel.isDone()) {
            throw new Exception("Create connection to " + ip + ":" + port + " timeout!");
        }
        if (channel.isCancelled()) {
            throw new Exception("Create connection to " + ip + ":" + port + " cancelled by user!");
        }
        if (!channel.isSuccess()) {
            throw new Exception("Create connection to " + ip + ":" + port + " error " + channel.cause());
        }
    }

    public abstract ChannelHandler getHandler();

    public void close() {
        if (this.channel == null) {
            return;
        }
        this.channel.channel().close();
    }

}
