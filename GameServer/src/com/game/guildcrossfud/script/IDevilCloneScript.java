package com.game.guildcrossfud.script;

import com.game.map.script.ICrossCloneScript;
import com.game.map.structs.MapObject;


/**
 * @Desc TODO
 * @Date 2021/2/23 15:43
 * @Auth ZUncle
 */
public interface IDevilCloneScript extends ICrossCloneScript {

    /**
     * 福地关闭
     */
    void fudClose(MapObject mapObject);

}
