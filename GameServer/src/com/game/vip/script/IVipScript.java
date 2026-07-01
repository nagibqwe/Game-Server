package com.game.vip.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

public interface IVipScript extends IScript {

    /**
     * 上线推送红点
     * @param player
     */

    void online(Player player);

    /**
     * 增加vip经验
     * @param player
     * @param exp
     * @param reason
     * @param actionId
     */
    void addVipExp(Player player, int exp, int reason, long actionId);

    /**
     * 获取vip信息
     * @param player
     */
    void getVipInfo(Player player);

    /**
     * 根据角色VIP经验获取
     * @param vipExp
     * @return
     */
    int getVipLvByExp(long vipExp);

    /**
     * 获取每日奖励
     * @param player
     */
    void getVipReward(Player player);

    /**
     * 获取VIP免费赠送奖励
     * @param player
     */
    void getVipFreeReward(Player player);

    /**
     * 购买等级礼包
     * @param player
     * @param lv
     */
    void purVipReward(Player player, int lv);

    /**
     * 上线推送vip充值金额
     * @param player
     */
    void onlineVipRechage(Player player);

    /**
     * 上线推送vip充值奖励
     * @param player
     */
    void onlineVipRechageRewardList(Player player);

    /**
     * vip充值奖励
     * @param player
     */
    void getVipRechageReward(Player player, int id);

    /**
     * 根据充值累计数量  检测特有vip
     */
    void checkSpecialVip(Player player);
}
