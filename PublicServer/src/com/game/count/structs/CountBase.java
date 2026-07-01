package com.game.count.structs;

/**
 * @Desc TODO
 * @Date 2020/11/17 10:16
 * @Auth ZUncle
 */
public enum CountBase {

    Variant(0),                 //变量数据
    PeakWeekStageBox(1),        //巅峰竞技场段位礼包

    ;
    final int value;
    final int hour;

    CountBase(int value, int hour) {
        this.value = value;
        this.hour = hour;
    }

    CountBase(int value) {
        this.value = value;
        this.hour = 0;
    }

    public int getHour() {
        return hour;
    }

    public int getValue() {
        return value;
    }
}
