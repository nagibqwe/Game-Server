/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
//import com.game.server.codec.ClientMessageCodeC;
import game.core.net.codec.ClientMessageCodeC;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 客户端的通道设置处理
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class ClientChannelImpl extends ChannelInitializer<SocketChannel> {

    private final int iTimeout;//是否设置超时秒值，如果为0则不设置
    private final ByteBufAllocator allocator = new PooledByteBufAllocator(true);

    /**
     * 设置网络的队列处理器， 及设置是否超时读取检查
     *
     * @param timeOut 超时秒值， 如果为0则不设置
     */
    public ClientChannelImpl(int timeOut) {
        iTimeout = timeOut;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        //设置缓存的分配器--每一个socket都对应一个Allocator
        //一个Allocator有多个threadCache,但是一个thread只能与一个threadCache进行绑定，绑定成功后，就不能再次改变了
        //是否会出现一个Allocator下的treadCache个数不能被所有的thread 平均分配
        //ThreadCache --用来针对某个线程下的内存分配，如果所有的线程对象共用一个
        SocketChannelConfig config = ch.config();
        config.setAllocator(allocator);
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(2048 * 1024, 0, 4));//最长接收2M的数据
        //加入解码器
        if (iTimeout > 0) {
            ch.pipeline().addLast("readTimeOut", new ReadTimeoutHandler(iTimeout));//30秒验证超过
        }
        ch.pipeline().addLast(new ClientMessageCodeC(30));//解码
        ch.pipeline().addLast(new ClientHandlerAdapter());//消息处理函数

    }
}
