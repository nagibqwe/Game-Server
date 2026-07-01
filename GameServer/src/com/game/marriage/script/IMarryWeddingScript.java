package com.game.marriage.script;

import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;

/**
 * @Desc TODO
 * @Date 2021/7/9 14:08
 * @Auth ZUncle
 */
public interface IMarryWeddingScript extends IMapBaseScript {


    /**
     * 发送滚屏
     * @param map
     * @param player
     * @param context
     */
    void reqMarrySendBulletScreen(MapObject map, Player player, String context);

    /**
     * 婚宴送礼
     * @param map
     * @param player
     * @param target
     * @param gift
     */
    void weddingGift(MapObject map, Player player, Player target, int gift, int count);

    /**
     *  放烟花
     * @param map
     * @param player
     * @param gift
     */
    void weddingUseGift(MapObject map, Player player, int gift);

    /**
     * 购买热度
     * @param map
     * @param player
     */
    void weddingBuyHot(MapObject map, Player player);
}
