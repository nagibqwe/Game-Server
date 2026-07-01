/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.gm.script;

import com.game.player.structs.Player;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.P2GGMCMDResult;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IGmScript {

    public boolean RunGmCmd(Player player, String command);

    public void OnPublicGMBack(ChannelHandlerContext context, P2GGMCMDResult mess);

    public void OnFightGM(ChannelHandlerContext context, CrossServerMessage.G2FGMdeal gfgm);
}
