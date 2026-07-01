package com.game.publicserver.structs;

import com.game.server.GameServer;
import com.game.server.filter.InnerChannelImpl;
import com.game.structs.GlobalType;
import game.core.util.HttpUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lw
 */
public class ConnectPublicServer implements Runnable {
    private final static Logger logger = LogManager.getLogger(ConnectPublicServer.class);

    public final int id = 10000;
    public final String publicIp;
    public final int publicPort;

    private volatile boolean mStop = false;
    private final NioEventLoopGroup workGroup = new NioEventLoopGroup();
    private final Bootstrap bootstrap = new Bootstrap();


    public ConnectPublicServer(String publicIp, int publicPort, int serverType) {
        this.publicIp = publicIp;
        this.publicPort = publicPort;
        bootstrap.group(workGroup).handler(new InnerChannelImpl(serverType));
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 2048);
        bootstrap.option(ChannelOption.SO_SNDBUF, 1024 * 2048);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        try {
            HttpUtils.sendPost(GlobalType.HEART_WEB, String.format(GlobalType.HEART_PARA, "connectpublic", GameServer.getInstance().getServerId(), 0, "init"));
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    @Override
    public void run() {
        connect();
        GameServer.isConnectOver = true;
    }

    public void stop() {
        mStop = true;
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }

    public void connect() {
        int connected = 0;
        while (connected < 1) {
            if (mStop) {
                return;
            }
//            logger.error("尝试连接公共服 ss={}:{}",publicIp, publicPort);
            ChannelFuture connect = bootstrap.connect(publicIp, publicPort);
            connect.awaitUninterruptibly(60 * 1000);
            if (!connect.isSuccess()) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    logger.error(e, e);
                }
                continue;
            }
            connected++;
            HttpUtils.sendPost(GlobalType.HEART_WEB, String.format(GlobalType.HEART_PARA, "connectpublic", GameServer.getInstance().getServerId(), 0, "start"));
        }
    }

}
