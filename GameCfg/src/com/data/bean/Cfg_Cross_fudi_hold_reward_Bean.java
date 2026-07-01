/**
 * Auto generated, do not edit it
 *
 * Cross_fudi_hold_reward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Cross_fudi_hold_reward_Bean{
    /**
     * ID
     */
    private final int id;
    /**
     * ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 福地等级（1，一级福地；2，二级福地；3，三级福地）
     */
    private final int position;
    /**
     * 福地等级（1，一级福地；2，二级福地；3，三级福地）
     * @return
     */
    public final int getPosition(){
        return position;
    }
    /**
     * 开服时间和世界等级控制
     */
    private final ReadIntegerArray day;
    /**
     * 开服时间和世界等级控制
     * @return
     */
    public final ReadIntegerArray getDay(){
        return day;
    }
    /**
     * 开服时间和世界等级控制
     */
    private final ReadIntegerArray level;
    /**
     * 开服时间和世界等级控制
     * @return
     */
    public final ReadIntegerArray getLevel(){
        return level;
    }
    /**
     * 排名
     */
    private final ReadIntegerArrayEs rank;
    /**
     * 排名
     * @return
     */
    public final ReadIntegerArrayEs getRank(){
        return rank;
    }
    /**
     * 职业男的奖励物品id
     */
    private final ReadIntegerArrayEs reward0;
    /**
     * 职业男的奖励物品id
     * @return
     */
    public final ReadIntegerArrayEs getReward0(){
        return reward0;
    }
    /**
     * 职业女的奖励物品id
     */
    private final ReadIntegerArrayEs reward1;
    /**
     * 职业女的奖励物品id
     * @return
     */
    public final ReadIntegerArrayEs getReward1(){
        return reward1;
    }
    /**
     * 职业3的奖励物品id
     */
    private final ReadIntegerArrayEs reward2;
    /**
     * 职业3的奖励物品id
     * @return
     */
    public final ReadIntegerArrayEs getReward2(){
        return reward2;
    }
    /**
     * 职业4的奖励物品id
     */
    private final ReadIntegerArrayEs reward3;
    /**
     * 职业4的奖励物品id
     * @return
     */
    public final ReadIntegerArrayEs getReward3(){
        return reward3;
    }

    public Cfg_Cross_fudi_hold_reward_Bean(int id,int position,String dayStr,String levelStr,String rankStr,String reward0Str,String reward1Str,String reward2Str,String reward3Str){
        this.id = id;
        this.position = position;
        this.day = new ReadIntegerArray(dayStr,",");
        this.level = new ReadIntegerArray(levelStr,",");
        this.rank = new ReadIntegerArrayEs(rankStr,"}",",");
        this.reward0 = new ReadIntegerArrayEs(reward0Str,"}",",");
        this.reward1 = new ReadIntegerArrayEs(reward1Str,"}",",");
        this.reward2 = new ReadIntegerArrayEs(reward2Str,"}",",");
        this.reward3 = new ReadIntegerArrayEs(reward3Str,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("position:").append(position).append(";");
        str.append("day:").append(day).append(";");
        str.append("level:").append(level).append(";");
        str.append("rank:").append(rank).append(";");
        str.append("reward0:").append(reward0).append(";");
        str.append("reward1:").append(reward1).append(";");
        str.append("reward2:").append(reward2).append(";");
        str.append("reward3:").append(reward3).append(";");
        return str.toString();
    }
}
