package com.game.marriage.command;

import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.marriage.script.IMarryWeddingScript;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.ICommand;

/**
 * @Desc TODO
 * @Date 2021/7/19 15:31
 * @Auth ZUncle
 */
public class WeddingBuyHotCommand implements ICommand {

    MapObject map;
    Player player;

    public WeddingBuyHotCommand(MapObject map, Player player) {
        this.map = map;
        this.player = player;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {

        IMapBaseScript base = Manager.mapManager.base(ScriptEnum.MarryWeddingActivityScript);
        if (base instanceof IMarryWeddingScript) {
            ((IMarryWeddingScript) base).weddingBuyHot(map, player);
        }

    }
}
