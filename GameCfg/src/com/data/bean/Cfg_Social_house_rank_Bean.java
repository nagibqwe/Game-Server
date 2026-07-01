/**
 * Auto generated, do not edit it
 *
 * social_house_rank配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Social_house_rank_Bean{
    /**
     * 
唯一ID
     */
    private final int ID;
    /**
     * 
唯一ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 轮数（1:每月1日-14日；2：每月15日-月底）
     */
    private final int turn;
    /**
     * 轮数（1:每月1日-14日；2：每月15日-月底）
     * @return
     */
    public final int getTurn(){
        return turn;
    }
    /**
     * 排名
     */
    private final ReadIntegerArray rank;
    /**
     * 排名
     * @return
     */
    public final ReadIntegerArray getRank(){
        return rank;
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
     * 展示奖励模型
     */
    private final int showreward;
    /**
     * 展示奖励模型
     * @return
     */
    public final int getShowreward(){
        return showreward;
    }

    public Cfg_Social_house_rank_Bean(int ID,int turn,String rankStr,String rewardStr,int showreward){
        this.ID = ID;
        this.turn = turn;
        this.rank = new ReadIntegerArray(rankStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.showreward = showreward;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("turn:").append(turn).append(";");
        str.append("rank:").append(rank).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("showreward:").append(showreward).append(";");
        return str.toString();
    }
}
