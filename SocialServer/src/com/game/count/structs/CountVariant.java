package com.game.count.structs;

/**
 * @Desc TODO
 * @Date 2020/11/17 10:01
 * @Auth ZUncle
 */
public enum CountVariant {

    HouseVote(1, CountReset.Day,0),                //家装大赛票数
    HouseRandomVote(2, CountReset.Day,0),          //家装大赛随机票数

    ;
    final int key;
    final CountReset reset;
    final int hour;
    final int minute;
    final int second;

    CountVariant(int key, CountReset reset) {
        this.key = key;
        this.reset = reset;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
    }
    CountVariant(int key, CountReset reset, int hour) {
        this.key = key;
        this.reset = reset;
        this.hour = hour;
        this.minute = 0;
        this.second = 0;
    }
    CountVariant(int key, CountReset reset, int hour, int minute) {
        this.key = key;
        this.reset = reset;
        this.hour = hour;
        this.minute = minute;
        this.second = 0;
    }
    CountVariant(int key, CountReset reset, int hour,  int minute, int second) {
        this.key = key;
        this.reset = reset;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getKey() {
        return key;
    }

    public CountReset getReset() {
        return reset;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
}
