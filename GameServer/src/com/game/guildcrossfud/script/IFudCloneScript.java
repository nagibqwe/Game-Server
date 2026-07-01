package com.game.guildcrossfud.script;

import com.game.map.script.ICrossCloneScript;
import com.game.map.structs.MapObject;


/**
 * @Desc TODO
 * @Date 2021/2/23 15:43
 * @Auth ZUncle
 */
public interface IFudCloneScript extends ICrossCloneScript {

    /**
     *  福地刷新怪物
     * @param mapObject
     * @param
     */
    void refreshBoss(MapObject mapObject);

    /**
     * 福地关闭
     */
    void fudClose(MapObject mapObject);

    /**
     * 活动2-8点 踢出玩家
     */
    void fudTickOut(MapObject map);

    /**
     *  刷新天禁值
     * @param mapObject
     * @param
     */
    void refreshTv(MapObject mapObject);

}
