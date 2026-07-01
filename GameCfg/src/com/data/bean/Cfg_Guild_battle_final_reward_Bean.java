/**
 * Auto generated, do not edit it
 *
 * guild_battle_final_reward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Guild_battle_final_reward_Bean{
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
     * 排名类型（0，福地排名；1，个人排名）
     */
    private final int type;
    /**
     * 排名类型（0，福地排名；1，个人排名）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 排名区间
     */
    private final ReadIntegerArray rank;
    /**
     * 排名区间
     * @return
     */
    public final ReadIntegerArray getRank(){
        return rank;
    }
    /**
     * 奖励列表
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励列表
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 描述
     */
    private final String des;
    /**
     * 描述
     * @return
     */
    public final String getDes(){
        return des;
    }

    public Cfg_Guild_battle_final_reward_Bean(int id,int type,String rankStr,String rewardStr,String des){
        this.id = id;
        this.type = type;
        this.rank = new ReadIntegerArray(rankStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.des = des;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("rank:").append(rank).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("des:").append(des).append(";");
        return str.toString();
    }
}
