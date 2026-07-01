/**
 * Auto generated, do not edit it
 *
 * treasure配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Treasure_Bean{
    /**
     * 奖励库id
     */
    private final int id;
    /**
     * 奖励库id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 所属抽奖（1低级寻宝免费机会；2高级寻宝免费机会；3低级寻宝；4高级寻宝）
     */
    private final int type;
    /**
     * 所属抽奖（1低级寻宝免费机会；2高级寻宝免费机会；3低级寻宝；4高级寻宝）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 抽中概率（万分比）
     */
    private final int probability;
    /**
     * 抽中概率（万分比）
     * @return
     */
    public final int getProbability(){
        return probability;
    }
    /**
     * 奖励内容（ID_数量）(@;@_@)
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励内容（ID_数量）(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 绑定类型(0：不绑定；1：获得时绑定)
     */
    private final int bind;
    /**
     * 绑定类型(0：不绑定；1：获得时绑定)
     * @return
     */
    public final int getBind(){
        return bind;
    }
    /**
     * 是否公告（0不公告；1公告）
     */
    private final int radio;
    /**
     * 是否公告（0不公告；1公告）
     * @return
     */
    public final int getRadio(){
        return radio;
    }
    /**
     * 职业ID，每个职业使用单独的奖励库
     */
    private final int professionalID;
    /**
     * 职业ID，每个职业使用单独的奖励库
     * @return
     */
    public final int getProfessionalID(){
        return professionalID;
    }

    public Cfg_Treasure_Bean(int id,int type,int probability,String rewardStr,int bind,int radio,int professionalID){
        this.id = id;
        this.type = type;
        this.probability = probability;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.bind = bind;
        this.radio = radio;
        this.professionalID = professionalID;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("probability:").append(probability).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("bind:").append(bind).append(";");
        str.append("radio:").append(radio).append(";");
        str.append("professionalID:").append(professionalID).append(";");
        return str.toString();
    }
}
