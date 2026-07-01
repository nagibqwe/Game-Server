package com.game.RechargeRebate.script;

import com.game.player.structs.Player;

public interface IRechargeRebate {


    /**
     * 加载数据
     */
    void loadData();
    /**
     * 返利
     */
    void rechargeRebate(Player player);

    /**
     * 统计充值数据
     */
    void addTotalRecharge(Player player,int totalfen);
}
