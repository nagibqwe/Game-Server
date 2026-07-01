package com.game.marriage.script;

import com.game.map.script.IMapBaseScript;
import com.game.player.structs.Player;

/**
 * @Desc TODO
 * @Date 2020/8/18 20:20
 * @Auth ZUncle
 */
public interface IMarryCloneScript extends IMapBaseScript {

    /**
     * 请求 伴侣购买次数
     * @param player
     */
    void reqCallMarryCloneBuy(Player player);


    /**
     * 购买 仙缘副本次数
     * @param player
     */
    void reqMarryCloneBuy(Player player);

    /**
     * 拒绝购买
     * @param player
     */
    void reqRefuseMarryClone(Player player);

    /**
     * 选择 默契大考验奖励
     */
    void reqSelectMarryClone(Player player, Integer img);

    /**
     * 同步情缘副本
     * @param player
     */
    void sendMarryCloneTimes(Player player);

}
