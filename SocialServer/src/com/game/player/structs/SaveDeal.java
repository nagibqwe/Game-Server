package com.game.player.structs;

/**
 * @Desc TODO
 * @Date 2021/6/23 17:40
 * @Auth ZUncle
 */
public enum SaveDeal {

    RightNow(-1),                   //立即保存
    OneMinLater(60 * 1000),         //1分总后保存
    FiveMinLater(5 * 60 * 1000),    //5分钟后保存
    TenMinLater(10 * 60 * 1000)     //10分钟后保存
    ;
    final int time;

    SaveDeal(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
