package com.game.server.social;

import com.game.db.DBErrorToFile;
import com.game.server.GameServer;
import com.game.structs.GlobalType;
import game.core.message.SMessage;
import game.core.net.Config.ServerConfig;
import game.core.util.HttpUtils;
import game.core.util.SessionUtils;
import game.core.util.TimeUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static game.core.util.SessionUtils.SEND_BUF;

/**
 * @Desc TODO
 * @Date 2021/6/11 15:27
 * @Auth ZUncle
 */
public class SocialServerClient implements Runnable {

    static Logger logger = LogManager.getLogger(SocialServerClient.class);

    public String ServerIP;
    public int Port;
    public boolean init = false;

    private volatile boolean mStop = false;
    private final NioEventLoopGroup workGroup = new NioEventLoopGroup();
    private final Bootstrap bootstrap = new Bootstrap();

    public ChannelHandlerContext channel;   //社交服务器连接

    public void init(String serverIP, int port) {
        init = true;
        ServerIP = serverIP;
        Port = port;
        bootstrap.group(workGroup).handler(new SocialChannelImpl());
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 2048);
        bootstrap.option(ChannelOption.SO_SNDBUF, 1024 * 2048);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        try {
            logger.info("当前社交服务器连接HTTP为：" + GlobalType.HEART_WEB + String.format(GlobalType.HEART_PARA, "ConnectSocial", GameServer.getInstance().getServerId(), 0, "init"));
            HttpUtils.sendPost(GlobalType.HEART_WEB, String.format(GlobalType.HEART_PARA, "ConnectSocial", GameServer.getInstance().getServerId(), 0, "init"));
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    public SocialServerClient setServerIP(String serverIP) {
        ServerIP = serverIP;
        return this;
    }

    public SocialServerClient setPort(int port) {
        Port = port;
        return this;
    }

    private enum Singleton {

        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        SocialServerClient manager;

        Singleton() {
            this.manager = new SocialServerClient();
        }

        SocialServerClient getProcessor() {
            return manager;
        }
    }

    //FriendManager
    public static SocialServerClient getInstance() {
        return SocialServerClient.Singleton.INSTANCE.getProcessor();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (!mStop) {
//            logger.info("尝试连接社交服务器++++++++++");
            ChannelFuture connect = bootstrap.connect(ServerIP, Port);
            connect.awaitUninterruptibly(60 * 1000);
            if (!connect.isSuccess()) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    logger.error(e, e);
                }
                continue;
            }
            HttpUtils.sendPost(GlobalType.HEART_WEB, String.format(GlobalType.HEART_PARA, "ConnectSocial", GameServer.getInstance().getServerId(), 0, "start"));
            try {
                synchronized (this) {
                    wait();
                }
            } catch (Exception e) {
                DBErrorToFile.error("SocialServerClient Thread wait Exception:" + e.getMessage());
            }
        }
    }

    public void stop() {
        mStop = true;
        workGroup.shutdownGracefully();
        try {
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            DBErrorToFile.error("SocialServerClient Thread Notify Exception:" + e.getMessage());
        }
    }

    public void connect() {
        try {
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            DBErrorToFile.error("SocialServerClient Thread Notify Exception:" + e.getMessage());
        }
    }

    public void sendToSocial(SMessage message) {
        if (channel != null) {
            ByteBuf buf = null;
            try {
                int len = (Integer.SIZE + Long.SIZE + Byte.SIZE) / Byte.SIZE + message.getData().length;
                buf = Unpooled.compositeBuffer(len + Integer.SIZE);
                buf.writeInt(len);
                buf.writeByte(0);
                buf.writeInt(message.getId());
                buf.writeLong(message.getSender());
                buf.writeBytes(message.getData());
                synchronized (channel) {
                    ByteBuf out = channel.channel().attr(SEND_BUF).get();
                    if (out == null) {
                        channel.channel().attr(SEND_BUF).set(buf);
                    } else {
                        out.writeBytes(buf);
                        buf.release();
                    }
                }
            } catch (Exception e) {
                logger.error("world处理消息协议时出错了，！", e);
            }
            return;
        }
        if (!TimeUtils.isIDEEnvironment()) {
           // logger.error("发送社交服消息时， 连接已经不存在了！， ID= " + message.getId());
            GameServer.getInstance().setErrorLog("game send to Social", " 连接已经断开了，发不了内容！");
        }
    }

    /**
     * 处理发送缓存队列的接口
     */
    public void BufferSend() {
        if (channel == null) {
            return;
        }
        //发送消息
        synchronized (channel) {
            if (channel.channel() == null) {
                logger.info(channel + "发送队列时， 连接已经断开了！1");
                return;
            }
            if (channel.channel().unsafe() == null) {
                logger.info(channel.channel() + "发送队列时， 连接已经断开了！2");
                return;
            }
            if (!channel.channel().isActive()) {
                logger.info(channel.channel() + "发送队列时， 连接已经断开了！4");
                channel.channel().attr(SessionUtils.SEND_BUF).set(null);
                return;
            }
            if (channel.channel().unsafe().outboundBuffer() == null) {
                logger.info(channel.channel() + "发送队列时， 连接已经断开了！3");
                return;
            }
            if (!channel.channel().isWritable()) {
                logger.info(channel.channel() + "发送队列时，暂时不可写！ size=" + channel.channel().unsafe().outboundBuffer().totalPendingWriteBytes());
                return;
            }
            ByteBuf buf = channel.channel().attr(SessionUtils.SEND_BUF).get();
            if (buf != null) {
                channel.channel().attr(SessionUtils.SEND_BUF).set(null);
            }
            if (buf != null && channel.channel().isActive()) {
                ChannelFuture cf = channel.writeAndFlush(buf);
            }
        }

    }
}
