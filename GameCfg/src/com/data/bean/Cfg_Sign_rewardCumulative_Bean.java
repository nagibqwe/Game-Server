/**
 * Auto generated, do not edit it
 *
 * sign_rewardCumulative配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Sign_rewardCumulative_Bean{
    /**
     * 签到累计奖励
     */
    private final int Id;
    /**
     * 签到累计奖励
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 代表第几轮
     */
    private final int round;
    /**
     * 代表第几轮
     * @return
     */
    public final int getRound(){
        return round;
    }
    /**
     * 继续下一轮，没有不填
     */
    private final int nextRound;
    /**
     * 继续下一轮，没有不填
     * @return
     */
    public final int getNextRound(){
        return nextRound;
    }
    /**
     * 累计天数
     */
    private final int day;
    /**
     * 累计天数
     * @return
     */
    public final int getDay(){
        return day;
    }
    /**
     * ID_num_是否绑定（0否 1是）
     */
    private final ReadIntegerArrayEs award;
    /**
     * ID_num_是否绑定（0否 1是）
     * @return
     */
    public final ReadIntegerArrayEs getAward(){
        return award;
    }

    public Cfg_Sign_rewardCumulative_Bean(int Id,int round,int nextRound,int day,String awardStr){
        this.Id = Id;
        this.round = round;
        this.nextRound = nextRound;
        this.day = day;
        this.award = new ReadIntegerArrayEs(awardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("round:").append(round).append(";");
        str.append("nextRound:").append(nextRound).append(";");
        str.append("day:").append(day).append(";");
        str.append("award:").append(award).append(";");
        return str.toString();
    }
}
