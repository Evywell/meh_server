package fr.evywell.common.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

import java.nio.charset.StandardCharsets;

public class BasicServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;
    private final ChannelHandler handler;

    public BasicServerInitializer(SslContext sslCtx, ChannelHandler handler) {
        this.sslCtx = sslCtx;
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (this.sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        pipeline.addLast("frameDecoder",
            new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4)
        );
        pipeline.addLast("decoder", new ByteArrayDecoder());
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
        pipeline.addLast("bytesEncoder", new ByteArrayEncoder());
        pipeline.addLast(this.handler);
    }

}
