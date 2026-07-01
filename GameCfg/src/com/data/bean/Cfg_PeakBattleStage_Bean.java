/**
 * Auto generated, do not edit it
 *
 * peakBattleStage配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_PeakBattleStage_Bean{
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
     * 段位
     */
    private final int stage;
    /**
     * 段位
     * @return
     */
    public final int getStage(){
        return stage;
    }
    /**
     * 星级
     */
    private final int star;
    /**
     * 星级
     * @return
     */
    public final int getStar(){
        return star;
    }
    /**
     * 段位Icon
     */
    private final String icon;
    /**
     * 段位Icon
     * @return
     */
    public final String getIcon(){
        return icon;
    }
    /**
     * 段位名称
     */
    private final String name;
    /**
     * 段位名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 积分
     */
    private final int integral;
    /**
     * 积分
     * @return
     */
    public final int getIntegral(){
        return integral;
    }
    /**
     * 奖励道具
     */
    private final ReadIntegerArrayEs stageReward;
    /**
     * 奖励道具
     * @return
     */
    public final ReadIntegerArrayEs getStageReward(){
        return stageReward;
    }
    /**
     * 可领取次数
     */
    private final int limitTimes;
    /**
     * 可领取次数
     * @return
     */
    public final int getLimitTimes(){
        return limitTimes;
    }
    /**
     * 胜利积分
     */
    private final int winIntegral;
    /**
     * 胜利积分
     * @return
     */
    public final int getWinIntegral(){
        return winIntegral;
    }
    /**
     * 失败积分
     */
    private final int loseIntegral;
    /**
     * 失败积分
     * @return
     */
    public final int getLoseIntegral(){
        return loseIntegral;
    }
    /**
     * 前十场胜利额外奖励
     */
    private final ReadIntegerArrayEs winExtraReward;
    /**
     * 前十场胜利额外奖励
     * @return
     */
    public final ReadIntegerArrayEs getWinExtraReward(){
        return winExtraReward;
    }
    /**
     * 前十场失败额外奖励
     */
    private final ReadIntegerArrayEs loseExtraReward;
    /**
     * 前十场失败额外奖励
     * @return
     */
    public final ReadIntegerArrayEs getLoseExtraReward(){
        return loseExtraReward;
    }
    /**
     * 胜利经验奖励值
     */
    private final int winExp;
    /**
     * 胜利经验奖励值
     * @return
     */
    public final int getWinExp(){
        return winExp;
    }
    /**
     * 失败经验奖励值
     */
    private final int loseExp;
    /**
     * 失败经验奖励值
     * @return
     */
    public final int getLoseExp(){
        return loseExp;
    }

    public Cfg_PeakBattleStage_Bean(int Id,int stage,int star,String icon,String name,int integral,String stageRewardStr,int limitTimes,int winIntegral,int loseIntegral,String winExtraRewardStr,String loseExtraRewardStr,int winExp,int loseExp){
        this.Id = Id;
        this.stage = stage;
        this.star = star;
        this.icon = icon;
        this.name = name;
        this.integral = integral;
        this.stageReward = new ReadIntegerArrayEs(stageRewardStr,"}",",");
        this.limitTimes = limitTimes;
        this.winIntegral = winIntegral;
        this.loseIntegral = loseIntegral;
        this.winExtraReward = new ReadIntegerArrayEs(winExtraRewardStr,"}",",");
        this.loseExtraReward = new ReadIntegerArrayEs(loseExtraRewardStr,"}",",");
        this.winExp = winExp;
        this.loseExp = loseExp;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("stage:").append(stage).append(";");
        str.append("star:").append(star).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("name:").append(name).append(";");
        str.append("integral:").append(integral).append(";");
        str.append("stageReward:").append(stageReward).append(";");
        str.append("limitTimes:").append(limitTimes).append(";");
        str.append("winIntegral:").append(winIntegral).append(";");
        str.append("loseIntegral:").append(loseIntegral).append(";");
        str.append("winExtraReward:").append(winExtraReward).append(";");
        str.append("loseExtraReward:").append(loseExtraReward).append(";");
        str.append("winExp:").append(winExp).append(";");
        str.append("loseExp:").append(loseExp).append(";");
        return str.toString();
    }
}
