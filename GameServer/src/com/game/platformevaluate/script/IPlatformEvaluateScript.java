/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.platformevaluate.script;

import com.game.player.structs.Player;
import game.message.PlatformEvaluateMessage;

/**
 * 点赞、分享和评价管理
 */
public interface IPlatformEvaluateScript {

    void onReqEvaluate(Player player, PlatformEvaluateMessage.ReqEvaluate messInfo);

    void playerOnline(Player player);

    /**
     * GM后台设置评价状态
     * @param player
     */
    void updateEvaluate(Player player, int type, boolean newState);


     void zeroClockDeal(Player player);

    void levelUp(Player player);
}
