/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.filter;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.QuitGameDefine;
import com.game.player.structs.SessionAttribute;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class MyOutClientHandler extends ChannelHandlerAdapter {

    private static final Logger log = LogManager.getLogger("MyOutClientHandler");

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        try {
            if (ctx.channel().attr(SessionAttribute.PLAYER).get() != null) {
                Player player = ctx.channel().attr(SessionAttribute.PLAYER).get();
                if (player != null) {
                    Manager.playerManager.iQuitGame().QuitGame(ctx, QuitGameDefine.MyOutClient, false, true);
                    log.info(ctx.channel() + " 已经注册了MyOutClientHandler 玩家:" + player.getName() + "(" + player.getId() + ")");
                } else {
                    log.info(ctx.channel() + " 连接还没有注册成功玩家信息！MyOutClientHandler");
                }
            } else {
                log.info(ctx.channel() + " 连接还没有注册成功玩家信息！MyOutClientHandler");
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

}
