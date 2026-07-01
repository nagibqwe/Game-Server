/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.backpack.script;

import com.game.player.structs.Player;
import game.message.backpackMessage.*;

/**
 * 背包协议接口
 *
 * @author Administrator
 */
public interface IBackpackScript {
    //背包整理
    void OnReqBagClearUp(Player player, ReqBagClearUp messInfo);

    //合成
    void OnReqCompound(Player player, ReqCompound messInfo);

    //拆分物品
    void OnReqMoveItem(Player player, ReqMoveItem messInfo);

    //请求元宝开启格子
    void OnReqOpenBagCell(Player player, ReqOpenBagCell messInfo);

    //物品出售
    void OnReqSellItems(Player player, ReqSellItems messInfo);

    //使用物品
    void OnReqUseItem(Player player, ReqUseItem messInfo);

    //检查物品使用后产生的BUFF
    void onReqUseItemMakeBuff(Player player, ReqUseItemMakeBuff messInfo);

    //快捷使用背包中的经验丹和银元宝袋
    void onReqAutoUseItem(Player player);
}
