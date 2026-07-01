/**
 * Auto generated, do not edit it
 *
 * peakBattleRank配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_PeakBattleRank_Bean{
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
     * 排名区间
     */
    private final ReadIntegerArray stage;
    /**
     * 排名区间
     * @return
     */
    public final ReadIntegerArray getStage(){
        return stage;
    }
    /**
     * 排行奖励
     */
    private final ReadIntegerArrayEs rewards;
    /**
     * 排行奖励
     * @return
     */
    public final ReadIntegerArrayEs getRewards(){
        return rewards;
    }

    public Cfg_PeakBattleRank_Bean(int Id,String stageStr,String rewardsStr){
        this.Id = Id;
        this.stage = new ReadIntegerArray(stageStr,",");
        this.rewards = new ReadIntegerArrayEs(rewardsStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("stage:").append(stage).append(";");
        str.append("rewards:").append(rewards).append(";");
        return str.toString();
    }
}
