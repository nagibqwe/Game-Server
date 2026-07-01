package com.game.task.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.taskMessage;

public interface IMainTaskScript extends IScript {

    /**
     * 执行主线任务
     */
    int doMainTask(Player player);

    /**
     * 主线任务改变检查
     * @param messInfo
     */
    void mainTaskChange(Player player, taskMessage.mainTaskInfo messInfo);

    /**
     * 主线任务完成
     * @param messInfo
     */
    void mainTaskFinish(Player player, taskMessage.ResTaskFinish messInfo);

    /**
     * 执行装备熔炼(回收)操作
     * @return
     */
    int doEquipSmelt(Player player);
}
