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
 * @Date 2021/7/9 16:20
 * @Auth ZUncle
 */
public class WeddingUseGiftCommand implements ICommand {

    MapObject map;
    Player player;
    int gift;

    public WeddingUseGiftCommand(MapObject map, Player player, int gift) {
        this.map = map;
        this.player = player;
        this.gift = gift;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {

        IMapBaseScript base = Manager.mapManager.base(ScriptEnum.MarryWeddingActivityScript);
        if (base instanceof IMarryWeddingScript) {
            ((IMarryWeddingScript) base).weddingUseGift(map, player, gift);
        }


    }
}
