/**
 * Auto generated, do not edit it
 *
 * world_question_reward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_World_question_reward_Bean{
    /**
     * 档次
     */
    private final int id;
    /**
     * 档次
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 最小积分
     */
    private final int level_min;
    /**
     * 最小积分
     * @return
     */
    public final int getLevel_min(){
        return level_min;
    }
    /**
     * 最大积分
     */
    private final int level_max;
    /**
     * 最大积分
     * @return
     */
    public final int getLevel_max(){
        return level_max;
    }
    /**
     * 奖励(@;@_@)
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_World_question_reward_Bean(int id,int level_min,int level_max,String rewardStr){
        this.id = id;
        this.level_min = level_min;
        this.level_max = level_max;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("level_min:").append(level_min).append(";");
        str.append("level_max:").append(level_max).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
