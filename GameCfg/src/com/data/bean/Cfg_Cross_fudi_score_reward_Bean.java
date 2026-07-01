/**
 * Auto generated, do not edit it
 *
 * Cross_fudi_score_reward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Cross_fudi_score_reward_Bean{
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
     * 奖励需要分数
     */
    private final int need;
    /**
     * 奖励需要分数
     * @return
     */
    public final int getNeed(){
        return need;
    }
    /**
     * 奖励物品
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励物品
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 需要付费的对应元宝金额
     */
    private final int if_pay;
    /**
     * 需要付费的对应元宝金额
     * @return
     */
    public final int getIf_pay(){
        return if_pay;
    }

    public Cfg_Cross_fudi_score_reward_Bean(int id,int need,String rewardStr,int if_pay){
        this.id = id;
        this.need = need;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.if_pay = if_pay;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("need:").append(need).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("if_pay:").append(if_pay).append(";");
        return str.toString();
    }
}
