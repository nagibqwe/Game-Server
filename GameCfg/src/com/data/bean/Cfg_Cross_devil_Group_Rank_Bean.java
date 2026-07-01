/**
 * Auto generated, do not edit it
 *
 * Cross_devil_Group_Rank配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Cross_devil_Group_Rank_Bean{
    /**
     * id
     */
    private final int id;
    /**
     * id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 对应Cross_devil_Group_Copy主键
     */
    private final int copyType;
    /**
     * 对应Cross_devil_Group_Copy主键
     * @return
     */
    public final int getCopyType(){
        return copyType;
    }
    /**
     * 排名下限
     */
    private final int lower_Limit;
    /**
     * 排名下限
     * @return
     */
    public final int getLower_Limit(){
        return lower_Limit;
    }
    /**
     * 排名上限
     */
    private final int upper_Limit;
    /**
     * 排名上限
     * @return
     */
    public final int getUpper_Limit(){
        return upper_Limit;
    }
    /**
     * 世界等级区间
     */
    private final ReadIntegerArray world_Level_Limit;
    /**
     * 世界等级区间
     * @return
     */
    public final ReadIntegerArray getWorld_Level_Limit(){
        return world_Level_Limit;
    }
    /**
     * 对应奖励
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 对应奖励
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_Cross_devil_Group_Rank_Bean(int id,int copyType,int lower_Limit,int upper_Limit,String world_Level_LimitStr,String rewardStr){
        this.id = id;
        this.copyType = copyType;
        this.lower_Limit = lower_Limit;
        this.upper_Limit = upper_Limit;
        this.world_Level_Limit = new ReadIntegerArray(world_Level_LimitStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("copyType:").append(copyType).append(";");
        str.append("lower_Limit:").append(lower_Limit).append(";");
        str.append("upper_Limit:").append(upper_Limit).append(";");
        str.append("world_Level_Limit:").append(world_Level_Limit).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
