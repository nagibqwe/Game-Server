package com.game.guildcrossfud.timer;

import com.game.guildcrossfud.script.IFudCloneScript;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.script.structs.ScriptEnum;
import game.core.command.ICommand;

/**
 * @Desc TODO
 * @Date 2021/8/14 14:21
 * @Auth ZUncle
 */
public class ClearTvEvent implements ICommand {

    MapObject map;

    public ClearTvEvent(MapObject map) {
        this.map = map;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        IMapBaseScript base = Manager.mapManager.base(ScriptEnum.CrossFudCloneScript);
        if (base instanceof IFudCloneScript) {
            ((IFudCloneScript) base).refreshTv(map);
        }
    }
}
