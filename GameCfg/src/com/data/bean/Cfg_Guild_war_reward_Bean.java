/**
 * Auto generated, do not edit it
 *
 * guild_war_reward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Guild_war_reward_Bean{
    /**
     * 编号
     */
    private final int id;
    /**
     * 编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 连胜场数
     */
    private final int count;
    /**
     * 连胜场数
     * @return
     */
    public final int getCount(){
        return count;
    }
    /**
     * 连胜奖励
物品_数量_绑定
物品：对应item表主键
数量：对应奖励的数量
绑定：0未绑定1绑定
     */
    private final ReadIntegerArrayEs continueReward;
    /**
     * 连胜奖励
物品_数量_绑定
物品：对应item表主键
数量：对应奖励的数量
绑定：0未绑定1绑定
     * @return
     */
    public final ReadIntegerArrayEs getContinueReward(){
        return continueReward;
    }
    /**
     * 终结奖励
     */
    private final ReadIntegerArrayEs endReward;
    /**
     * 终结奖励
     * @return
     */
    public final ReadIntegerArrayEs getEndReward(){
        return endReward;
    }
    /**
     * 盘古BUFF，当守城方处于连胜状态的时候，给攻城方增加的BUFF；对应BUFF表的ID
     */
    private final int panguBuff;
    /**
     * 盘古BUFF，当守城方处于连胜状态的时候，给攻城方增加的BUFF；对应BUFF表的ID
     * @return
     */
    public final int getPanguBuff(){
        return panguBuff;
    }

    public Cfg_Guild_war_reward_Bean(int id,int count,String continueRewardStr,String endRewardStr,int panguBuff){
        this.id = id;
        this.count = count;
        this.continueReward = new ReadIntegerArrayEs(continueRewardStr,"}",",");
        this.endReward = new ReadIntegerArrayEs(endRewardStr,"}",",");
        this.panguBuff = panguBuff;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("count:").append(count).append(";");
        str.append("continueReward:").append(continueReward).append(";");
        str.append("endReward:").append(endReward).append(";");
        str.append("panguBuff:").append(panguBuff).append(";");
        return str.toString();
    }
}
