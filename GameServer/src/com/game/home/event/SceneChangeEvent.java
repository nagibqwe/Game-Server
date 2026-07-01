package com.game.home.event;

import com.game.home.script.IHomeSceneScript;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import game.core.command.ICommand;
import game.core.script.IScript;

/**
 * @Desc TODO
 * @Date 2021/12/16 11:15
 * @Auth ZUncle
 */
public class SceneChangeEvent implements ICommand {

    MapObject map;
    int level;

    public SceneChangeEvent(MapObject map, int level) {
        this.map = map;
        this.level = level;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {

        IScript is = Manager.scriptManager.GetScriptClass(map.getSetting().getIsscript());
        if (is instanceof IHomeSceneScript) {
            ((IHomeSceneScript) is).doSceneChange(map, level);
        }
    }
}
