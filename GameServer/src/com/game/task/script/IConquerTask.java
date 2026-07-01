package com.game.task.script;

import com.game.player.structs.Player;
import com.game.task.structs.ConquerTask;
import game.message.taskMessage;

/**
 * 宗派任务单独接口
 */
public interface IConquerTask {
    /**
     * 直接完成任务，不做消耗、位置判断
     * @param player
     * @param task
     * @param finishPer 奖励倍率
     */
    void noResumeFinishTask(Player player, ConquerTask task, int finishPer);

    /**
     * 一键完成所有帮会日常、周常任务
     * @param player  玩家
     * @param subType 子类型
     */
    void oneKeyFinishAllConquerTask(Player player, int subType);

    /**
     * 一键完成宗派任务
     * @param player
     * @param mess
     * @return
     */
    boolean conquerTaskOneKeyFinish(Player player, taskMessage.ReqOneKeyOverTask mess);

    /**
     * 检查玩家是否在一个npc或者雕像附近
     * @param player
     * @param npcId
     * @param statueId
     * @return
     */
    boolean nearNpcOrStatue(Player player, int npcId, int statueId);

    void onReqRefreshGuildTaskPool(Player player, taskMessage.ReqRefreshGuildTaskPool messInfo);

    void onReqGiveUpTask(Player player, taskMessage.ReqGiveUpTask messInfo);

    void refreshGuildTaskPool(Player player);

    void online(Player player);

    void zeroClockDeal(Player player);

    taskMessage.conquerTaskInfo.Builder buildTaskMessage(Player player, int taskId);

    void sendResGuildTaskPool(Player player);
}
