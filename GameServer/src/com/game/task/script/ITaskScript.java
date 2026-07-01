package com.game.task.script;

import com.data.struct.ReadLongArrayEs;
import com.game.player.structs.Player;
import com.game.task.structs.Task;
import game.core.script.IScript;

/**
 * 各类型任务通用接口
 */
public interface ITaskScript extends IScript {
    /**
     * 处理玩家的任务
     */
    void computeTask(Player player,boolean isLogin, boolean isRefresh);

    /**
     * 接任务
     * @param player
     * @param modelId
     * @param isLogin 是否是登录接取任务
     */
    void acceptTask(Player player, int modelId, int subType, boolean isLogin, boolean isRefresh);

    /**
     * 完成任务
     * @param player    玩家
     * @param modelId   任务模式
     * @param taskId    任务的实际ID
     * @param finishPer 完成任务得奖倍数
     * @param isGm
     * @return 是否完成了任务
     */
    boolean onFinishTask(Player player, int modelId, int taskId, int finishPer, boolean isGm, int subType);

    /**
     * 协议的组装者
     * @param player
     * @param task
     * @return
     */
    Object buildTaskInfo(Player player, Task task);

    /**
     * 完成当前任务之后
     * @param player
     * @param currentModelId
     */
    void taskFinishAfter(Player player, int currentModelId, int subType, int targetType);

    /**
     * 向客户端同步任务进度(接新任务、进度改变都走这个)
     * @param player
     * @param task
     */
    void changeTask(Player player, Task task, boolean isRefresh);

    /**
     * 获取当前任务的奖励字符串
     * @param player
     * @param task
     * @param rate   奖励倍数
     * @return 奖励字符串
     */
    ReadLongArrayEs getRewardArray(Player player, Task task, int rate);

    /**
     * 检查玩家的某个任务是否已经完成
     * @param player
     * @param taskId
     * @return
     */
    boolean checkTaskIsFinish(Player player, int taskId);

    /**
     * 设置任务当前进度
     * @param player
     * @param task
     */
    void updateTaskProgress(Player player, Task task);

    /**
     * 通知客户端删除某个类型的某个任务
     * @param player
     * @param taskType
     * @param modelId
     */
    void noticeDeleteTask(Player player, int taskType, int modelId);

    /**
     * 接取任务记log
     * @param player
     * @param task
     */
    void writeAcceptTaskLog(Player player, Task task);

    /**
     * 完成任务记log
     * @param player
     * @param task
     */
    void writeFinishTaskLog(Player player, Task task);
}
