package com.game.activityRanklist.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**限时活动排行榜脚本*/
public interface IActivityRankScript extends IScript {

    /**获取排行榜信息*/
    void getActivityRankInfo(Player player, int id);

    /**排行榜领奖*/
    void getActivityAward(Player player, int rankType, int awardId);

    /**timer。每十秒钟执行一次*/
    void tick(long nowDay, long lastCheckDay);

    /**检测是否有奖励可领，如果有，则推送*/
    void checkAwardAvailable(long playerId, int rankType, long rankValue);
}
