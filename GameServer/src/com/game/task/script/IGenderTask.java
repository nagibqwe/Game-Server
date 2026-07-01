package com.game.task.script;

import com.game.player.structs.Player;

/**
 * 转职任务接口
 */
public interface IGenderTask {
    /**
     * 用来加载转职任务的缓存
     */
    void loadGenderTaskCache();

    /**
     * 玩家等级提升的时候处理转职任务
     * @param player
     */
    void levelUpDealGenderTask(Player player);

    /**
     * 一键完成转职任务
     * @param player
     */
    void oneKeyFinishAllGenderTask(Player player, boolean isGm);
}
