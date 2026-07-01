package com.game.server.filter;

import game.core.net.codec.InnerMessageCodeC;
import game.core.net.codec.LengthFieldFrameDecoder;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;

/**
 * @Desc TODO
 * @Date 2021/6/12 18:42
 * @Auth ZUncle
 */
public class ServerChannelImpl extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //设置缓存的分配器--每一个socket都对应一个Allocator
        //一个Allocator有多个threadCache,但是一个thread只能与一个threadCache进行绑定，绑定成功后，就不能再次改变了
        //是否会出现一个Allocator下的treadCache个数不能被所有的thread 平均分配
        //ThreadCache --用来针对某个线程下的内存分配，如果所有的线程对象共用一个
        SocketChannelConfig config = ch.config();
        config.setAllocator(new PooledByteBufAllocator(true));
        //加入解码器
        ch.pipeline().addFirst(new LengthFieldFrameDecoder(Integer.MAX_VALUE, 0, 4));//最长接收2M的数据
        ch.pipeline().addLast(new InnerMessageCodeC());//解码
        ch.pipeline().addLast(new ServerMessageAdapter());//处理函数


    }
}
