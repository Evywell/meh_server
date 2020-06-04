package fr.evywell.robclient.network;

import fr.evywell.common.logger.Log;
import fr.evywell.common.network.Packet;
import fr.evywell.common.network.RequestFoundation;
import fr.evywell.common.opcode.Handler;
import fr.evywell.common.opcode.OpcodeHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private OpcodeHandler opcodeHandler;

    public ClientChannelInitializer(OpcodeHandler opcodeHandler) {
        this.opcodeHandler = opcodeHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new StringEncoder(), new StringDecoder(), new ClientChannelHandler(opcodeHandler));
    }

    public static class ClientChannelHandler extends ChannelInboundHandlerAdapter {

        private OpcodeHandler opcodeHandler;

        public ClientChannelHandler(OpcodeHandler opcodeHandler) {
            this.opcodeHandler = opcodeHandler;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            RequestFoundation rq = RequestFoundation.fromString((String) msg);
            Log.info(String.format("Nouveau packet: %s", msg));
            Handler handler = this.opcodeHandler.getHandler(rq.getCmd());
            Log.info("Traitement du packet avec l'OPCODE " + rq.getCmd());

            handler.call(null, rq.getBody().read(handler.getPayloadTemplate()), new Packet());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            Log.error("Un message envoyé par le serveur possède un payload inattendu: " + cause.getMessage());
        }
    }

}
