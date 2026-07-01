package com.game.dailyactivity.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

public interface IDailyActivityScript extends IScript {

    /**
     * 请求进入日常活动
     */
    int enterDaily(Player player, int dailyId);

    /**
     * 请求进入日常活动
     */
    int enterDaily(Player player, int dailyId, int modelId);
}
