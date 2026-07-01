package com.game.openserverac.scripts;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * V4返利
 */
public interface IV4Rebate extends IScript {


    /**
     * 上线初始化
     * @param player
     */
    void onLineInit(Player player);


    /**
     * 提交任务
     * @param player
     * @param id
     */
    void onReqV4RebateCompleteTask(Player player,int id);


    /**
     * 领取阶段奖励
     * @param player
     * @param rewardState
     */
    void onReqV4RebeteReward(Player player,int rewardState);


    /**
     * 进度刷新
     * @param player
     * @param type
     */
    void onReqV4RebeteUpProgress(Player player,int type,int count);

}
