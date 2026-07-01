package com.game.bigplayer.struct;


/**
 * CXL
 * 大玩咖数据
 */
public class BigPlayerData {


    private int getTitle ;//称号领取进度

    private int getFashion;//时装领取

    private int getReward;//礼包等奖励领取


    private QQReturnData qqReturnData;

    public QQReturnData getQqReturnData() {
        return qqReturnData;
    }

    public void setQqReturnData(QQReturnData qqReturnData) {
        this.qqReturnData = qqReturnData;
    }
}
