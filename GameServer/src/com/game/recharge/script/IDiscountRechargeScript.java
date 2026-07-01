package com.game.recharge.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.RechargeMessage;

/**
 * @Desc TODO 超值折扣
 * @Date 2020/8/7 11:37
 * @Auth ZUncle
 */
public interface IDiscountRechargeScript extends IScript {

    /**
     *  请求超值折扣数据
     * @param player
     * @param mess
     */
    void reqDiscountRecharge(Player player, RechargeMessage.ReqDiscountRecharge mess);

    /**
     *  超值折扣充值检测
     */
    void checkDiscountRecharge(Player player, RechargeMessage.ReqCheckDiscRechargeGoods mess);

    /**
     *  超值折扣充值 开始倒计时
     */
    void checkDiscountRecharge(Player player);

    /**
     * 玩家上线
     * @param player
     */
    void online(Player player);

    /**
     * 玩家下线检测 超值充值
     * @param player
     */
    void offline(Player player);


    /**
     * 获取剩余时间
     * @param player
     * @param cfgGoodId
     * @return
     */
    long getRemainTime(Player player,int cfgGoodId);

    /**
     * 购买商品
     * @param player
     * @param messInfo
     */
    void reqDiscRechargeBuyGoods(Player player, RechargeMessage.ReqDiscRechargeBuyGoods messInfo);

    /**
     * 领取免费超值折扣
     * @param player
     * @param messInfo
     */
    void reqGetFreeDiscGoods(Player player, RechargeMessage.ReqGetFreeDiscGoods messInfo);
}
