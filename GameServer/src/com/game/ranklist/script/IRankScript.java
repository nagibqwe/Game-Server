package com.game.ranklist.script;

import com.game.db.bean.RankPlayer;
import game.core.script.IScript;
import game.message.RankListMessage.RankInfo;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 排行榜接口
 */
public interface IRankScript extends IScript {

    /**
     * 排行榜类型
     */
    int getRankType();

    /**
     * 特定类型的排行榜网络消息
     */
    List<RankInfo.Builder> getRankInfo();

    /**
     * 检查是否可以进入排行榜
     */
    boolean canRank(RankPlayer rankPlayer);

    /**
     * 排序
     */
    int compareRankPlayer(RankPlayer p1, RankPlayer p2);

    /**该排行榜对比的值*/
    long getCompareValue(RankPlayer rankPlayer);

}
