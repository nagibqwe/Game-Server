package com.game.home.script;

import com.game.map.structs.MapObject;

/**
 * @Desc TODO
 * @Date 2021/12/16 11:11
 * @Auth ZUncle
 */
public interface IHomeSceneScript {

    /**
     * 处理场景刷新
     * @param map
     * @param level
     */
    void doSceneChange(MapObject map, int level);

}
