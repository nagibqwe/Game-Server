/**
 * Auto generated, do not edit it
 *
 * new_sever_rankrew配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_New_sever_rankrew_Bean{
    /**
     * id,id=type*100+sort
     */
    private final int id;
    /**
     * id,id=type*100+sort
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 读取比拼的表里面的ID
     */
    private final int type;
    /**
     * 读取比拼的表里面的ID
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 全服奖励（1）和个人奖励类型（0）
     */
    private final int subType;
    /**
     * 全服奖励（1）和个人奖励类型（0）
     * @return
     */
    public final int getSubType(){
        return subType;
    }
    /**
     * 用于客户端排序
     */
    private final int sort;
    /**
     * 用于客户端排序
     * @return
     */
    public final int getSort(){
        return sort;
    }
    /**
     * 排名的最低等级
只要到达限制条件
     */
    private final int minRank;
    /**
     * 排名的最低等级
只要到达限制条件
     * @return
     */
    public final int getMinRank(){
        return minRank;
    }
    /**
     * 排名的最高等级
     */
    private final int maxRank;
    /**
     * 排名的最高等级
     * @return
     */
    public final int getMaxRank(){
        return maxRank;
    }
    /**
     * 限制的条件，必须满足排行和限制条件才可领取
     */
    private final int Limit;
    /**
     * 限制的条件，必须满足排行和限制条件才可领取
     * @return
     */
    public final int getLimit(){
        return Limit;
    }
    /**
     * 奖励配置
     */
    private final ReadIntegerArrayEs rew;
    /**
     * 奖励配置
     * @return
     */
    public final ReadIntegerArrayEs getRew(){
        return rew;
    }
    /**
     * 对应打开排行榜页签
     */
    private final int rankType;
    /**
     * 对应打开排行榜页签
     * @return
     */
    public final int getRankType(){
        return rankType;
    }

    public Cfg_New_sever_rankrew_Bean(int id,int type,int subType,int sort,int minRank,int maxRank,int Limit,String rewStr,int rankType){
        this.id = id;
        this.type = type;
        this.subType = subType;
        this.sort = sort;
        this.minRank = minRank;
        this.maxRank = maxRank;
        this.Limit = Limit;
        this.rew = new ReadIntegerArrayEs(rewStr,"}",",");
        this.rankType = rankType;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("subType:").append(subType).append(";");
        str.append("sort:").append(sort).append(";");
        str.append("minRank:").append(minRank).append(";");
        str.append("maxRank:").append(maxRank).append(";");
        str.append("Limit:").append(Limit).append(";");
        str.append("rew:").append(rew).append(";");
        str.append("rankType:").append(rankType).append(";");
        return str.toString();
    }
}
