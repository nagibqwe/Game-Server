package com.game.task.script;

import com.game.player.structs.Player;
import com.game.task.structs.TaskCondition;
import java.util.HashMap;
import java.util.List;

/**
 * 处理一些等级不同任务就不同的任务
 * @author admin
 */
public interface ILevelTask {
    
    /**
     * 根据玩家自己的信息来确定当前可接的一些任务 条件为 等级、阵营、职业等
     *
     * @param player
     * @param isFinal 是否是最后的额外奖励
     * @return
     */
    int getCanReceiveTask(Player player, int subType, boolean isFinal);
    
    /**
     * 获取每种任务不同等级阶段的可选任务
     *
     * @param type 任务类型
     * @return
     */
    HashMap<TaskCondition, List<Integer>> loadLevelTask(int type);
    
}
