/**
 * Auto generated, do not edit it
 *
 * JJCAeward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_JJCAeward_Bean{
    /**
     * 奖励的唯一ID
     */
    private final int id;
    /**
     * 奖励的唯一ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 排名区间(@_@)
     */
    private final ReadIntegerArray rank;
    /**
     * 排名区间(@_@)
     * @return
     */
    public final ReadIntegerArray getRank(){
        return rank;
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

    public Cfg_JJCAeward_Bean(int id,String rankStr,String rewardStr){
        this.id = id;
        this.rank = new ReadIntegerArray(rankStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("rank:").append(rank).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
