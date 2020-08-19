package fr.evywell.robclient.network;

import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.RequestFoundation;
import fr.evywell.common.network.Session;
import fr.evywell.common.opcode.Handler;
import fr.evywell.common.opcode.OpcodeHandler;
import fr.evywell.robclient.display.Window;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private OpcodeHandler opcodeHandler;
    private Window window;

    public ClientChannelInitializer(OpcodeHandler opcodeHandler, Window window) {
        this.opcodeHandler = opcodeHandler;
        this.window = window;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("frameDecoder",
                new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4)
        );
        ch.pipeline().addLast("decoder", new ByteArrayDecoder());
        ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
        ch.pipeline().addLast("bytesEncoder", new ByteArrayEncoder());
        ch.pipeline().addLast(new ClientChannelHandler(opcodeHandler, window));
    }

    public static class ClientChannelHandler extends ChannelInboundHandlerAdapter {

        private OpcodeHandler opcodeHandler;
        private Window window;

        public ClientChannelHandler(OpcodeHandler opcodeHandler, Window window) {
            this.opcodeHandler = opcodeHandler;
            this.window = window;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String ipClient = ctx.channel().remoteAddress().toString();
            try {
                ByteBuf buffer = Unpooled.copiedBuffer((byte[])msg);
                byte[] bytes;
                int length = buffer.readableBytes();

                if (buffer.hasArray()) {
                    bytes = buffer.array();
                } else {
                    bytes = new byte[length];
                    buffer.getBytes(buffer.readerIndex(), bytes);
                }
                Log.info(String.format("Lecture de %d bytes", length));
                Packet packet = new Packet(bytes);
                window.getTextArea().setText(window.getTextArea().getText() + "\n OPCODE: " + packet.getOpcode());
                Handler handler = this.opcodeHandler.getHandler(packet.getOpcode());
                handler.call(null, handler.getPayload(packet), packet);
            } catch (Exception e) {
                Log.error(String.format("Paquet mal formé. Déconnexion du client... %s, cause: %s", ipClient, e.getMessage()));
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            Log.error("Un message envoyé par le serveur possède un payload inattendu: " + cause.getMessage());
        }
    }

}
