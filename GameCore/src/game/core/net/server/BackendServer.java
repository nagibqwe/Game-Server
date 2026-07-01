/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.net.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 监听端口连接处理
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class BackendServer extends BaseServer
{
    public BackendServer(String serverName, int port)
    {
        super(serverName, port);
        isListen = false;
    }

    private static final Logger log = LogManager.getLogger(BackendServer.class);

    private static int netCodeKey = 0x6B;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workGroup = new NioEventLoopGroup(2);
    private final ServerBootstrap server = new ServerBootstrap();
    private String serverName = "后台监听服务";
    private volatile boolean isListen = false;

    /**
     *
     * @param channel
     */
    public void start(ChannelInitializer<SocketChannel> channel)
    {
        if (isListen)
        {
            return;
        }
        /**
         * option()是提供给NioServerSocketChannel用来接收进来的连接,也就是boss线程。
         * childOption()是提供给由父管道ServerChannel接收到的连接，也就是worker线程
         */
        server.group(bossGroup, workGroup);
        server.childHandler(channel);
        server.channel(NioServerSocketChannel.class);
//        server.option(ChannelOption.SO_SNDBUF, 2048 * 2048);//发送缓冲器
        server.option(ChannelOption.SO_RCVBUF, 1024 * 1024);
        server.option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(512 * 1024, 4 * 1024 * 1024));
        server.childOption(ChannelOption.SO_SNDBUF, 1024 * 2048);
        server.childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_REUSEADDR, true);     //重用地址

        server.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        server.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        ChannelFuture f = server.bind(port);
        isListen = true;
        f.addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception
            {
                if (future.isSuccess())
                {
                    log.info(serverName + "监听" + port + "成功！");
                }
                else
                {
                    log.error(serverName + "监听" + port + "失败！退出");
                    System.exit(1);
                }
            }
        });
        try
        {
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException ex)
        {
            log.error(serverName + "监听" + port + "失败！退出");
            System.exit(1);
        }
    }

    @Override
    public void stop()
    {
        if (isListen)
        {
            log.error(serverName + "释放监听数组！");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
        isListen = false;
    }

    //设置errorLog 调用
    public void setErrorLog(String key, String content)
    {

    }

    protected void setNetCodeKey(int netCodeKey)
    {
        BackendServer.netCodeKey = netCodeKey;
    }

    public static int getNetCodeKey()
    {
        return netCodeKey;
    }

}
