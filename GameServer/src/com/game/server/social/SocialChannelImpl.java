package com.game.server.social;

import com.game.server.filter.InnerHandlerAdapter;
import com.game.server.filter.InnerPlayerHandlerAdapter;
import game.core.net.codec.InnerMessageCodeC;
import game.core.net.codec.LengthFieldFrameDecoder;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;

import java.util.concurrent.Executors;

/**
 * @Desc TODO
 * @Date 2021/6/22 20:01
 * @Auth ZUncle
 */
public class SocialChannelImpl extends ChannelInitializer<SocketChannel> {

    private final ByteBufAllocator allocator = new PooledByteBufAllocator(true);
    private final GlobalTrafficShapingHandler trafficShaping = new GlobalTrafficShapingHandler(Executors.newScheduledThreadPool(1), 5000);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        SocketChannelConfig config = ch.config();
        config.setAllocator(allocator);
        ch.pipeline().addFirst(new LengthFieldFrameDecoder(Integer.MAX_VALUE, 0, 4));//最长接收2M的数据
        ch.pipeline().addLast(trafficShaping);
        ch.pipeline().addLast(new InnerMessageCodeC());
        ch.pipeline().addLast(new SocialHandlerAdapter());
        ch.pipeline().addLast(new InnerPlayerHandlerAdapter());
    }
}
