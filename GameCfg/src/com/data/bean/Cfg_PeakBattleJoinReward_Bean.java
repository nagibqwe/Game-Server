/**
 * Auto generated, do not edit it
 *
 * peakBattleJoinReward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_PeakBattleJoinReward_Bean{
    /**
     * 唯一id
     */
    private final int Id;
    /**
     * 唯一id
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 参与场次
     */
    private final int joinTimes;
    /**
     * 参与场次
     * @return
     */
    public final int getJoinTimes(){
        return joinTimes;
    }
    /**
     * 参与奖励 //参与场次_道具ID_数量
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 参与奖励 //参与场次_道具ID_数量
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_PeakBattleJoinReward_Bean(int Id,int joinTimes,String rewardStr){
        this.Id = Id;
        this.joinTimes = joinTimes;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("joinTimes:").append(joinTimes).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
