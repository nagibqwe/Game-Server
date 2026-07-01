package com.game.dailyactive.script;

import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;

/**
 * 同步地图数据
 */
public interface IDailyScript extends IMapBaseScript {

    /**
     * 同步日常首领版面数据
     */
    void sendBossPanel(Player player, MapObject map);

    /**
     * 同步日常首领数据
     */
    void sendBossInfo(Player player, MapObject map);

    /**
     * 刷新阵营
     * @param player
     * @param map
     */
    void  changeCamp(Player player, MapObject map);
}
