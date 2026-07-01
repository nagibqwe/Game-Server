/**
 * Auto generated, do not edit it
 *
 * KaoShangLing_Horse配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_KaoShangLing_Horse_Bean{
    /**
     * key值
     */
    private final int id;
    /**
     * key值
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 轮数
     */
    private final int rank;
    /**
     * 轮数
     * @return
     */
    public final int getRank(){
        return rank;
    }
    /**
     * 分数
     */
    private final int score;
    /**
     * 分数
     * @return
     */
    public final int getScore(){
        return score;
    }
    /**
     * 普通奖励
     */
    private final ReadIntegerArrayEs common_reward;
    /**
     * 普通奖励
     * @return
     */
    public final ReadIntegerArrayEs getCommon_reward(){
        return common_reward;
    }
    /**
     * 高级奖励
     */
    private final ReadIntegerArrayEs specail_reward;
    /**
     * 高级奖励
     * @return
     */
    public final ReadIntegerArrayEs getSpecail_reward(){
        return specail_reward;
    }
    /**
     * 是否为最后一轮
     */
    private final int if_end;
    /**
     * 是否为最后一轮
     * @return
     */
    public final int getIf_end(){
        return if_end;
    }

    public Cfg_KaoShangLing_Horse_Bean(int id,int rank,int score,String common_rewardStr,String specail_rewardStr,int if_end){
        this.id = id;
        this.rank = rank;
        this.score = score;
        this.common_reward = new ReadIntegerArrayEs(common_rewardStr,"}",",");
        this.specail_reward = new ReadIntegerArrayEs(specail_rewardStr,"}",",");
        this.if_end = if_end;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("rank:").append(rank).append(";");
        str.append("score:").append(score).append(";");
        str.append("common_reward:").append(common_reward).append(";");
        str.append("specail_reward:").append(specail_reward).append(";");
        str.append("if_end:").append(if_end).append(";");
        return str.toString();
    }
}
