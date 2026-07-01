package com.game.vip.script;

import com.data.struct.ReadArray;
import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * VIP宝珠脚本
 */
public interface IVipPearlScript extends IScript {
    /**
     * 发送VIP珠宝信息
     */
    void activeVipPearl(Player player, int id);
    /**
     * 使用VIP珠宝道具
     * 效果：激活珠宝
     */
    void useVipPearlItem(Player player, int itemId, ReadArray<Integer> tab, int reason);

    /**
     * VIP珠宝过期检测
     */
    void vipPearlTimeOutCheck(Player player);

    /**
     * 发送VIP珠宝信息
     */
    void onlineVipPearlInfo(Player player);
}
