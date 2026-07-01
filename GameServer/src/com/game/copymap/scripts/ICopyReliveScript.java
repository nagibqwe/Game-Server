package com.game.copymap.scripts;

import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.core.map.Position;

/**
 * @Desc TODO
 * @Date 2021/10/9 17:25
 * @Auth ZUncle
 */
public interface ICopyReliveScript {

    /**
     * 计算副本复活点
     * @param player
     * @return
     */
    Position doCreateRelivePosition(MapObject map, Player player);

}
