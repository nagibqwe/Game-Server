package com.game.map.script;

import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.message.CommonMessage;

import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/2/18 13:56
 * @Auth ZUncle
 */
public interface ICrossCloneScript extends IMapBaseScript {

    /**
     *  玩家进入跨服副本参数信息
     * @param player
     * @param mapObject
     * @param cross
     */
    void enterCross(Player player, MapObject mapObject, List<CommonMessage.CrossAttribute> cross);

}
