package com.game.commercialize.inter;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * 商业化内容标准接口
 */
public interface ICommercialize extends IScript {
    /**
     * 玩家上线
     * @param player
     */
    void playerOnline(Player player);

    /**
     * 请求商业化内容
     * @param player
     */
    void onReqCommercialize(Player player);

    /**
     * 完成了一次充值
     * TODO 这里最好用商业化的配置做为参数
     * @param player
     * @param commercializeID 商业化订单ID
     * @param gold 充值元宝数量
     */
    void recharge(Player player, int commercializeID, int gold,int totalFee);
}
