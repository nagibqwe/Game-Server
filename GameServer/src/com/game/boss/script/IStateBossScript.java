package com.game.boss.script;

import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;

/**
 * @Desc TODO
 * @Date 2021/11/2 15:01
 * @Auth ZUncle
 */
public interface IStateBossScript extends IMapBaseScript {

    /**
     * 请求境界boss购买
     *
     * @param player
     */
    void doBuyBossCount(Player player);

    /**
     * 请求境界boss刷新
     *
     * @param player
     */
    void doRefreshBoss(Player player, MapObject mapObject);


    /**
     * 请求打开境界boss界面
     *
     * @param player
     */
    void doBossInfo(Player player);

}
