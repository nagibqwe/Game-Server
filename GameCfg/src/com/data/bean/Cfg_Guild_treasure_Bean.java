/**
 * Auto generated, do not edit it
 *
 * guild_treasure配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Guild_treasure_Bean{
    /**
     * key=rankmix
     */
    private final int id;
    /**
     * key=rankmix
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 名次多少开始
     */
    private final int rankmix;
    /**
     * 名次多少开始
     * @return
     */
    public final int getRankmix(){
        return rankmix;
    }
    /**
     * 名次多少结束
     */
    private final int rankmax;
    /**
     * 名次多少结束
     * @return
     */
    public final int getRankmax(){
        return rankmax;
    }
    /**
     * 完成任务获得的奖励，道具_数量(@;@_@)
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 完成任务获得的奖励，道具_数量(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_Guild_treasure_Bean(int id,int rankmix,int rankmax,String rewardStr){
        this.id = id;
        this.rankmix = rankmix;
        this.rankmax = rankmax;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("rankmix:").append(rankmix).append(";");
        str.append("rankmax:").append(rankmax).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
