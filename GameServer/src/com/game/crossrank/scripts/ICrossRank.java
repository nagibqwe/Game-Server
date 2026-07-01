package com.game.crossrank.scripts;

/**
 * Created by 542 on 2020/4/13.
 */
public interface ICrossRank {


    /**
     * 发送排行数据到公共服
     */
    void sendRankInfoToPublic();

    /**
     * 跨服世界等级
     * @param crossLevel
     */
    void onP2GCrossWorldLv(int crossLevel);

}
