package com.game.count.structs;

/**
 * @Desc TODO
 * @Date 2020/11/17 10:01
 * @Auth ZUncle
 */
public enum CountVariant {

    PeakRankCalc(1, CountReset.Week,24),            //巅峰竞技场赛季结算
    ZeroClock(2, CountReset.Day,0),                 //零点刷新
    FiveClock(3, CountReset.Day,5),                 //五点刷新
    PeakTimesMail(4, CountReset.Day),                     //巅峰竞技场次奖励
    AllocCrossFud(5, CountReset.Day,0,1),    //分配福地
    CrossFudScore(6, CountReset.Week,24),           //福地积分刷新
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
