package com.game.guildcrossfud.timer;

import com.game.guildcrossfud.script.IFudCloneScript;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.script.structs.ScriptEnum;
import game.core.command.ICommand;

/**
 * @Desc TODO
 * @Date 2021/3/3 16:07
 * @Auth ZUncle
 */
public class RefreshBossEvent implements ICommand {

    MapObject map;

    public RefreshBossEvent(MapObject map) {
        this.map = map;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        IMapBaseScript base = Manager.mapManager.base(ScriptEnum.CrossFudCloneScript);
        if (base instanceof IFudCloneScript) {
            ((IFudCloneScript) base).refreshBoss(map);
        }
    }
}
