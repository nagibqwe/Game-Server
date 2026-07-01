package com.game.marriage.command;

import com.game.guildcrossfud.script.IFudCloneScript;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.marriage.script.IMarryWeddingScript;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.ICommand;

/**
 * @Desc TODO
 * @Date 2021/7/9 11:37
 * @Auth ZUncle
 */
public class WeddingBulletScreenCommand implements ICommand {

    MapObject map;
    Player player;
    String context;

    public WeddingBulletScreenCommand(MapObject map, Player player, String context) {
        this.map = map;
        this.player = player;
        this.context = context;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {

        IMapBaseScript base = Manager.mapManager.base(ScriptEnum.MarryWeddingActivityScript);
        if (base instanceof IMarryWeddingScript) {
            ((IMarryWeddingScript) base).reqMarrySendBulletScreen(map, player, context);
        }

    }
}
