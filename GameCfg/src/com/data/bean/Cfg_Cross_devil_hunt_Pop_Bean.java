/**
 * Auto generated, do not edit it
 *
 * Cross_devil_hunt_Pop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Cross_devil_hunt_Pop_Bean{
    /**
     * 抽奖类型
     */
    private final int id;
    /**
     * 抽奖类型
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 抽奖一次增加封魔度
     */
    private final int rate;
    /**
     * 抽奖一次增加封魔度
     * @return
     */
    public final int getRate(){
        return rate;
    }
    /**
     * 抽奖次数_抽奖道具_消耗抽奖道具数量
     */
    private final ReadIntegerArrayEs times;
    /**
     * 抽奖次数_抽奖道具_消耗抽奖道具数量
     * @return
     */
    public final ReadIntegerArrayEs getTimes(){
        return times;
    }

    public Cfg_Cross_devil_hunt_Pop_Bean(int id,int rate,String timesStr){
        this.id = id;
        this.rate = rate;
        this.times = new ReadIntegerArrayEs(timesStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("rate:").append(rate).append(";");
        str.append("times:").append(times).append(";");
        return str.toString();
    }
}
