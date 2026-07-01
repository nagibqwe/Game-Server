package com.game.task.script;

import com.game.player.structs.Player;
import com.game.task.structs.BranchTask;

/**
 * 支线任务接口
 */
public interface IBranchScript extends ITaskScript {

    boolean action(Player player, BranchTask bt, int type, int num);

    /**
     * 0点清除过期限时任务
     * @param player
     */
    void zeroTaskClean(Player player);
}
