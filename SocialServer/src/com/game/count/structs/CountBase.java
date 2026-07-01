package com.game.count.structs;

/**
 * @Desc TODO
 * @Date 2020/11/17 10:16
 * @Auth ZUncle
 */
public enum CountBase {

    Variant(0),                 //变量数据
    HouseGift(1),               //家园送礼计数
    HouseGiftSend(2),           //家园已送玩家计数
    HouseVisitor(3),            //家园首次访问
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
