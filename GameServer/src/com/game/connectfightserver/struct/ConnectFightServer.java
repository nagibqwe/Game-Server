package com.game.connectfightserver.struct;

import com.game.player.structs.SessionAttribute;
import com.game.server.filter.InnerChannelImpl;
import game.core.net.Config.ServerEnum;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * 战斗服连接类
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class ConnectFightServer implements Runnable {

    private static final Logger log = LogManager.getLogger(ConnectFightServer.class);

    private final Bootstrap bp = new Bootstrap();

    private final NioEventLoopGroup workGroup = new NioEventLoopGroup();

    private final ChannelInitializer<SocketChannel> m_ClientServer;

    private String server_name = "连接战斗服";
    private String mIp;
    private int mPort;
    private final int mFightServerId;
    private boolean isConnect = false;
    private boolean connectBool = false;
    private boolean stop = false;
    private long lastheartTime = 0;
    private int fid;//战斗服的ID值
    private final Set<Integer> mapIds = new HashSet<>();

    public ConnectFightServer(int serverId, String ip, int port) {
        mIp = ip;
        mPort = port;
        mFightServerId = serverId;
        server_name += "_" + serverId;
        stop = false;

        m_ClientServer = new InnerChannelImpl(ServerEnum.FIGHT_CLIENT_LIMIT);
        //注册服务器关闭线程
        Runtime.getRuntime().addShutdownHook(new Thread(new CloseByExit(server_name)));
        bp.group(workGroup).handler(m_ClientServer);
        bp.option(ChannelOption.TCP_NODELAY, true);
        bp.channel(NioSocketChannel.class);
        bp.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true);     //重用地址
        bp.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    public boolean isTrue(String ip, int port) {
        return (ip.equalsIgnoreCase(mIp) && port == mPort && !isConnect && connectBool);
    }

    @Override
    public void run() {
        connect();
    }

    //改变了位置
    public void setIPPort(String ip, int port) {
        this.mIp = ip;
        this.mPort = port;
    }

    //连接是否成功
    public boolean isConnectBool() {
        return connectBool;
    }

    //是否正在连接
    public boolean isIsConnect() {
        return isConnect;
    }

    public void connect() {

        if (stop) {
            return;
        }

        isConnect = true;
        int connected = 0;
        int waitTime = 0;
        while (connected < 1) {
            //连接世界服务器
            ChannelFuture connect = bp.connect(this.mIp, this.mPort);
            connect.awaitUninterruptibly(60 * 1000);

            if (!connect.isSuccess()) {
                waitTime += 1;
                log.error("第" + waitTime + "次重新连接战斗服 id :" + this.mFightServerId + " -- " + this.mIp + " :" + this.mPort);
                //只给王次的机会
                if (waitTime > 4) {
                    connectBool = false;
                    isConnect = false;
                    return;
                }
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    log.error(e, e);
                }

                continue;
            }
            connected++;
            connectBool = true;
            isConnect = false;
            SocketChannel cl = (SocketChannel) connect.channel();
            cl.attr(SessionAttribute.CONNECT_SERVER_ID).set(this.mFightServerId);
            cl.attr(SessionAttribute.CONNECT_SERVER_IP).set(this.mIp);
            cl.attr(SessionAttribute.CONNECT_SERVER_PORT).set(this.mPort);
        }
    }

    private void stop() {
        stop = true;
        isConnect = false;
        connectBool = false;
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }

    public long getLastheartTime() {
        return lastheartTime;
    }

    public void setLastheartTime(long lastheartTime) {
        this.lastheartTime = lastheartTime;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public Set<Integer> getMapIds() {
        return mapIds;
    }

    class CloseByExit implements Runnable {

        //日志
        private final Logger log = LogManager.getLogger(CloseByExit.class);

        //服务器名字
        private final String server_name;

        public CloseByExit(String server_name) {
            this.server_name = server_name;
        }

        @Override
        public void run() {
            //执行关闭事件
            stop();
            log.info(this.server_name + " Stop!");
        }

    }
}
