package com.game.server.publicClient;

import com.game.db.DBErrorToFile;
import game.core.message.SMessage;
import game.core.util.SessionUtils;
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
 * @Desc TODO  公共服连接
 * @Date 2021/6/10 15:44
 * @Auth ZUncle
 */
public class PublicClient implements Runnable {

    static Logger logger = LogManager.getLogger(PublicClient.class);

    public final String serverIP;
    public final int port;

    private volatile boolean mStop = false;
    private final NioEventLoopGroup workGroup = new NioEventLoopGroup();
    private final Bootstrap bootstrap = new Bootstrap();

    public ChannelHandlerContext channel;


    public PublicClient(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;

        bootstrap.group(workGroup).handler(new PublicClientChannelImpl());
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 2048);
        bootstrap.option(ChannelOption.SO_SNDBUF, 1024 * 2048);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

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

//            logger.info("开始尝试连公共服+++++++++++");
            ChannelFuture connect = bootstrap.connect(serverIP, port);
            connect.awaitUninterruptibly(60 * 1000);
            if (!connect.isSuccess()) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    logger.error(e, e);
                }
                continue;
            }
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
            DBErrorToFile.error("SocialServerClient Thread notify Exception:" + e.getMessage());
        }
    }

    public void connect() {
        try {
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            DBErrorToFile.error("SocialServerClient Thread notify Exception:" + e.getMessage());
        }
    }

    public void sendToPublic(SMessage message) {
        if (channel == null) {
            logger.error("发送公共服消息时， 连接已经不存在了！， ID= " + message.getId());
            return;
        }
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
            logger.error("处理消息协议时出错了，！", e);
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
            ChannelHandlerContext temp = channel;
            if (temp.channel() == null) {
                logger.info(temp + "发送队列时， 连接已经断开了！1");
                return;
            }
            if (temp.channel().unsafe() == null) {
                logger.info(temp.channel() + "发送队列时， 连接已经断开了！2");
                return;
            }
            if (!temp.channel().isActive()) {
                logger.info(temp.channel() + "发送队列时， 连接已经断开了！4");
                temp.channel().attr(SessionUtils.SEND_BUF).set(null);
                return;
            }
            if (temp.channel().unsafe().outboundBuffer() == null) {
                logger.info(temp.channel() + "发送队列时， 连接已经断开了！3");
                return;
            }
            if (!temp.channel().isWritable()) {
                logger.info(temp.channel() + "发送队列时，暂时不可写！ size=" + temp.channel().unsafe().outboundBuffer().totalPendingWriteBytes());
                return;
            }
            ByteBuf buf = temp.channel().attr(SessionUtils.SEND_BUF).get();
            if (buf != null) {
                temp.channel().attr(SessionUtils.SEND_BUF).set(null);
            }
            if (buf != null && temp.channel().isActive()) {
                ChannelFuture cf = temp.writeAndFlush(buf);
            }
        }

    }

}
