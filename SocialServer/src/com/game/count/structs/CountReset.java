package com.game.count.structs;

/**
 * @Desc TODO
 * @Date 2020/11/17 9:51
 * @Auth ZUncle
 */
public enum CountReset {

    Forever(0), //永久
    Year(1), //每年刷新九零 一起玩 www.9 0175.com
    Month(2), //每月刷新
    Week(3), //每周刷新
    Day(4),  //每日刷新
    Hour(5),  //每小时刷新
    ;
    final int value;

    CountReset(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CountReset convert(int v) {
        for (CountReset type : CountReset.values()) {
            if (type.getValue() == v) {
                return type;
            }
        }
        return null;
    }

}
