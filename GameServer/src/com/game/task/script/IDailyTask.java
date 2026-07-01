package com.game.task.script;

import com.game.player.structs.Player;
import com.game.task.structs.DailyTask;
import game.message.taskMessage;

/**
 * 日常任务接口
 */
public interface IDailyTask {
    /**
     * 花钱完成任务
     * @param player
     * @param task
     * @param finishPer
     */
    void finishTask(Player player, DailyTask task, int finishPer);

    /**
     * 随机生成星级
     * @param taskId
     * @return
     */
    int randomStar(int taskId);

    /**
     * 处理玩家一键完成所有的日常任务
     * @param player
     * @param subType
     * @param checkCon 是否检查消耗条件
     */
    void oneKeyFinishAllDailyTask(Player player, int subType, boolean checkCon,int taskCount);

    /**
     * 一键完成日常任务
     * @param player
     * @param mess
     * @return
     */
    boolean dailyTaskOneKeyFinish(Player player, taskMessage.ReqOneKeyOverTask mess);


    /**
     * 请假环数奖励
     */
    void reqDailyTaskCountReward(Player player,taskMessage.ReqDailyTaskCountReward mess);

}
