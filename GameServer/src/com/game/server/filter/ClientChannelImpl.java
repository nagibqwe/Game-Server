package com.game.server.filter;

import game.core.net.codec.ClientMessageCodeC;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import java.util.concurrent.Executors;

public class ClientChannelImpl extends ChannelInitializer<SocketChannel> {

    private final int iTimeout;//是否设置超时秒值，如果为0则不设置
    private final ByteBufAllocator allocator = new PooledByteBufAllocator(true);
    private final GlobalTrafficShapingHandler trafficShaping = new GlobalTrafficShapingHandler(Executors.newScheduledThreadPool(1), 5000);

    public ClientChannelImpl(int timeOut) {
        iTimeout = timeOut;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        SocketChannelConfig config = ch.config();
        config.setAllocator(allocator);

        ch.pipeline().addFirst(new MyOutClientHandler());
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(4096 * 4096, 0, 4));//最长接收2M的数据
        ch.pipeline().addLast(trafficShaping);
        //加入解码器
        if (iTimeout > 0) {
            ch.pipeline().addLast("readTimeOut", new ReadTimeoutHandler(iTimeout));//30秒验证超过
        }
        ch.pipeline().addLast(new ClientMessageCodeC(60));//解码一秒来60条指令
        ch.pipeline().addLast(new ClientMsgAdapter());//消息处理函数

    }

    public GlobalTrafficShapingHandler getTrafficShaping() {
        return trafficShaping;
    }

}
