package com.game.guildcrossfud.timer;

import com.game.guildcrossfud.script.IDevilCloneScript;
import com.game.guildcrossfud.script.IFudCloneScript;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.script.structs.ScriptEnum;
import game.core.command.ICommand;

/**
 * @Desc TODO
 * @Date 2021/3/5 0:13
 * @Auth ZUncle
 */
public class FudCloseEvent implements ICommand {

    MapObject map;

    public FudCloseEvent(MapObject map) {
        this.map = map;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        IMapBaseScript base = Manager.mapManager.base(map.getSetting().getIsscript());
        if (base instanceof IFudCloneScript) {
            ((IFudCloneScript) base).fudClose(map);
        }
        if (base instanceof IDevilCloneScript) {
            ((IDevilCloneScript) base).fudClose(map);
        }
    }
}
