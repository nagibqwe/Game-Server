package com.game.welfare.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

public interface IWelfareScript extends IScript {
    /**
     * 玩家上线
     * @param player
     */
    void playerOnline(Player player);

    /**
     * 请求某福利子项数据
     * @param player
     */
    void freshDataNtf(Player player);
}
