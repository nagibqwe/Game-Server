package com.game.alienGem.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

public interface IAlienGemScript extends IScript {

    /**
     * 创建地图
     */
    public void createMap(int type);

    /**
     * 玩家进入地图
     */
    public void enterMap(Player player, int type);

    /**
     * 活动管理
     */
    void close();
}
