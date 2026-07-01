/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.http;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class SendBackFuture implements ChannelFutureListener {

    private static final Logger LOG = LogManager.getLogger("SendBackFuture");

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            LOG.error("back http result success!");
        } else {
            LOG.error("back http result failure!");
        }
    }
}
