/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.utils;

import com.game.player.structs.Player;
import game.core.map.IMapObject;
import game.core.message.SMessage;

/**
 *
 * @author zenghai
 */
public interface MessageInterFace {

    //玩家计算分级
    public void OnCalcFilterR(Player player);

    //广播接口1
    public void OnBroadcastRound2(IMapObject point, SMessage message);

    //广播接口2
    public void OnBroadcastRound3(IMapObject point, SMessage message, boolean includeMe);

}
