package com.game.task.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.taskMessage;

public interface ITaskScript extends IScript {

    /**
     * 请求改变任务状态
     * @param type
     * @param modelId
     */
    void reqChangeTaskState(Player player, int type, int modelId);
}
