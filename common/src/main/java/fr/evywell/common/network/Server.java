package fr.evywell.common.network;

import com.jsoniter.JsonIterator;
import fr.evywell.common.logger.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class Server {

    private static final int MAX_REQUEST_PER_CHANNEL = 100;

    private final int port;
    private ServerBootstrap bootstrap;
    private ChannelFuture channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Map<Integer, Integer> requests;

    private boolean running;
    private boolean acceptNewConnection;

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private Map<Integer, Session> sessions;

    public Server(int port) {
        this.port = port;
        this.bootstrap = new ServerBootstrap();
        this.running = false;
        this.acceptNewConnection = true;
        this.requests = new HashMap<>();
        this.sessions = new HashMap<>();
    }

    public void start() throws InterruptedException {
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true);

        this.channel = this.bootstrap.bind(this.port).sync();
        // BEFORE START
        this.running = true;
        this.beforeStarting();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    channel.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public abstract void beforeStarting();
    public abstract void afterStopping();

    public boolean canHandleRequest(int channel) {
        if (!this.requests.containsKey(channel)) {
            this.requests.put(channel, 1);
            return true;
        }
        Integer nbRequests = this.requests.get(channel);
        if (nbRequests >= MAX_REQUEST_PER_CHANNEL) {
            return false;
        }
        nbRequests += 1;
        this.requests.put(channel, nbRequests);

        return true;
    }

    public void addSession(Session session) {
        this.sessions.put(session.channel().hashCode(), session);
    }

    public void removeSession(int hash) {
        this.sessions.remove(hash);
    }

    public Session getSession(int hash) {
        if (this.sessions.containsKey(hash)) {
            return this.sessions.get(hash);
        }

        return null;
    }

    public List<Session> getSessions() {
        return new ArrayList<Session>(this.sessions.values());
    }

    public abstract void dispatch(int _cmd, Packet packet, Session session) throws MalformedRequestException;

    public void handle(ChannelHandler handler) {
        this.bootstrap.childHandler(handler);
    }

    public void stop() {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Log.info("Stopping request launched at " + format.format(date));
        this.running = false;
        // Fermeture de toutes les connexions
        for (Channel ch : this.channels) {
            if (ch.isOpen()) {
                ch.close();
            }
        }
        this.workerGroup.shutdownGracefully();
        this.bossGroup.shutdownGracefully();
        this.afterStopping();
    }

    public ChannelFuture getChannel() {
        return channel;
    }

    public ChannelGroup getChannels() {
        return channels;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isAcceptNewConnection() {
        return acceptNewConnection;
    }

    public void setAcceptNewConnection(boolean acceptNewConnection) {
        this.acceptNewConnection = acceptNewConnection;
    }
}
