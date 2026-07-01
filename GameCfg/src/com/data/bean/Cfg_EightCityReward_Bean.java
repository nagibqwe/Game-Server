/**
 * Auto generated, do not edit it
 *
 * EightCityReward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_EightCityReward_Bean{
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
     * 类型（1，每日奖励；2,赛季奖励）
     */
    private final int type;
    /**
     * 类型（1，每日奖励；2,赛季奖励）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 排名区间（包含两头）
     */
    private final ReadIntegerArray rank;
    /**
     * 排名区间（包含两头）
     * @return
     */
    public final ReadIntegerArray getRank(){
        return rank;
    }
    /**
     * 奖励内容
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励内容
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_EightCityReward_Bean(int id,int type,String rankStr,String rewardStr){
        this.id = id;
        this.type = type;
        this.rank = new ReadIntegerArray(rankStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("rank:").append(rank).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
