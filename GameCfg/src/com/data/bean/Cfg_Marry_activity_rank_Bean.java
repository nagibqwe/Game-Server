/**
 * Auto generated, do not edit it
 *
 * marry_activity_rank配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Marry_activity_rank_Bean{
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
     * 比拼内容，对应Rank_base的ID
     */
    private final int rankType;
    /**
     * 比拼内容，对应Rank_base的ID
     * @return
     */
    public final int getRankType(){
        return rankType;
    }
    /**
     * 全服排名奖励（1）
个人达成（0）
     */
    private final int rewardType;
    /**
     * 全服排名奖励（1）
个人达成（0）
     * @return
     */
    public final int getRewardType(){
        return rewardType;
    }
    /**
     * 最低名次
     */
    private final int minRank;
    /**
     * 最低名次
     * @return
     */
    public final int getMinRank(){
        return minRank;
    }
    /**
     * 最高名次
     */
    private final int maxRank;
    /**
     * 最高名次
     * @return
     */
    public final int getMaxRank(){
        return maxRank;
    }
    /**
     * 限制条件，所需最低亲密度
     */
    private final int limit;
    /**
     * 限制条件，所需最低亲密度
     * @return
     */
    public final int getLimit(){
        return limit;
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
     * 界面显示文字
     */
    private final String showstring;
    /**
     * 界面显示文字
     * @return
     */
    public final String getShowstring(){
        return showstring;
    }

    public Cfg_Marry_activity_rank_Bean(int id,int rankType,int rewardType,int minRank,int maxRank,int limit,String rewardStr,String showstring){
        this.id = id;
        this.rankType = rankType;
        this.rewardType = rewardType;
        this.minRank = minRank;
        this.maxRank = maxRank;
        this.limit = limit;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.showstring = showstring;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("rankType:").append(rankType).append(";");
        str.append("rewardType:").append(rewardType).append(";");
        str.append("minRank:").append(minRank).append(";");
        str.append("maxRank:").append(maxRank).append(";");
        str.append("limit:").append(limit).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("showstring:").append(showstring).append(";");
        return str.toString();
    }
}
