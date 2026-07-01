package com.game.soulanimalforest.script;

import com.data.bean.Cfg_Bossnew_SoulBeasts_Bean;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.core.script.IScript;

import java.util.List;

/**
 * 魂兽森林副本的特有接口
 */
public interface ISoulAnimalIslandClone extends IScript {

    void birthTime(int type, MapObject mapObject, long birthTime);

    /**
     * 同步跨服BOSS的信息
     * @param mapObject
     * @param configId
     * @param type
     */
    void syncCrossBossInfo(MapObject mapObject, int configId, int type);

    boolean canResetBossData(Player player, List<Cfg_Bossnew_SoulBeasts_Bean> beans, boolean all, boolean notify);

    /**
     * 刷新boss
     * @param player
     * @param all
     */
    void resetBossData(Player player, boolean all);

    int canCallBoss(Player player);

    /**
     * 召唤boss
     * @param player
     */
    void callBoss(Player player);

}
