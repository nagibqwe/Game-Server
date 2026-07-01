package com.game.universe.script;

import com.game.player.structs.Player;
import com.game.ranklist.script.IRankScript;
import game.core.script.IScript;
import game.message.MSG_UniverseMessage;

/**
 *  天墟战场排名面板信息
 */
public interface IUniverseRankScript extends IRankScript {
    /**
     * 请求天墟战场排名面板信息
     */
    void onReqUniverseRankPanel(Player player);

    /**
     * 天墟战场排名刷新
     */
    void sortUniverseRank();

    /**
     * 检查天墟战场阶段
     */
    void checkUniverseStage();

    /**
     * 同步天虚战场跨服阶段
     */
    void updateUniverseStage(int num);
}
