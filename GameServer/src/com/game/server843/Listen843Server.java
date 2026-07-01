/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server843;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 监听843端口
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class Listen843Server extends Thread {

    private static final Logger log = LogManager.getLogger("Listen843Server");

    public Listen843Server(){
        super("Listen843Server start");
    }

    @Override
    public void run() {

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
//                            if (sslCtx != null) {
//                                p.addLast(sslCtx.newHandler(ch.alloc()));
//                            }
                            ch.pipeline().addLast("readTimeOut", new ReadTimeoutHandler(30));//30秒验证超过
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(new Listen843Handler());
                        }
                    });
            final int port = 843;
            // Start the server.
            ChannelFuture f = b.bind(port).sync();

            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        log.error("监听" + port + "成功！");
                    } else {
                        log.error("监听" + port + "失败！退出");
                        System.exit(1);
                    }
                }
            });
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("843监听端口失败！", e);
        } finally {
            log.error("843监听端口失败了退出了！");
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
