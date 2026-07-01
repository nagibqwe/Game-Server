package com.game.copymap.scripts;

import com.game.map.script.IMapBaseScript;
import com.game.player.structs.Player;

/**
 * @Desc TODO 万妖卷
 * @Date 2021/8/19 14:43
 * @Auth ZUncle
 */
public interface ISingleTowerScript extends IMapBaseScript {

    /**
     * 发送副本数据
     * @param player
     */
    void sendSingleTowerInfo(Player player);

    /**
     * 下一波怪
     * @param player
     */
    void goOnChallenge(Player player);

    /**
     * 下一关
     * @param player
     */
    void ReqGotoNextChallenge(Player player);
}
