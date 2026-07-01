package com.game.bi.inter;

import com.game.bi.biqq.QQLogType;
import com.game.player.structs.Player;
import game.core.script.IScript;

public interface IQQScript extends IScript {


    /**
     * 日志记录
     *
     * @param player
     */
    void log(Player player, QQLogType type);

    /**
     * 重试
     */
    void retry();
}
