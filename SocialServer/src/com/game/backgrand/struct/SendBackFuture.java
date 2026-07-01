package com.game.backgrand.struct;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/8/16 11:14
 * @Auth ZUncle
 */
public class SendBackFuture implements ChannelFutureListener {

    static final Logger LOG = LogManager.getLogger(SendBackFuture.class);

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            LOG.error("back http result success!");
        } else {
            LOG.error("back http result failure!");
        }
    }
}