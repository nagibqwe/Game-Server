/**
 * Auto generated, do not edit it
 *
 * Cross_devil_hunt_Pool配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Cross_devil_hunt_Pool_Bean{
    /**
     * 奖池id
     */
    private final int id;
    /**
     * 奖池id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 封魔度限制
对应Cross_devil_hunt_Hot主键
-1代表公共部分，所有封魔度区间都可能抽到

     */
    private final int hot_Limit;
    /**
     * 封魔度限制
对应Cross_devil_hunt_Hot主键
-1代表公共部分，所有封魔度区间都可能抽到

     * @return
     */
    public final int getHot_Limit(){
        return hot_Limit;
    }
    /**
     * 奖励
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 高级抽奖权重
各个封魔度的权重独立计算
     */
    private final int probability;
    /**
     * 高级抽奖权重
各个封魔度的权重独立计算
     * @return
     */
    public final int getProbability(){
        return probability;
    }
    /**
     * 中级抽奖权重
各个封魔度的权重独立计算
     */
    private final int probabilityMiddle;
    /**
     * 中级抽奖权重
各个封魔度的权重独立计算
     * @return
     */
    public final int getProbabilityMiddle(){
        return probabilityMiddle;
    }
    /**
     * 低级抽奖权重
各个封魔度的权重独立计算
     */
    private final int probabilityLow;
    /**
     * 低级抽奖权重
各个封魔度的权重独立计算
     * @return
     */
    public final int getProbabilityLow(){
        return probabilityLow;
    }

    public Cfg_Cross_devil_hunt_Pool_Bean(int id,int hot_Limit,String rewardStr,int probability,int probabilityMiddle,int probabilityLow){
        this.id = id;
        this.hot_Limit = hot_Limit;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.probability = probability;
        this.probabilityMiddle = probabilityMiddle;
        this.probabilityLow = probabilityLow;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("hot_Limit:").append(hot_Limit).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("probability:").append(probability).append(";");
        str.append("probabilityMiddle:").append(probabilityMiddle).append(";");
        str.append("probabilityLow:").append(probabilityLow).append(";");
        return str.toString();
    }
}
