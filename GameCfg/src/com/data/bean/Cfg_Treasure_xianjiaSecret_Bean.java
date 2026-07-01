/**
 * Auto generated, do not edit it
 *
 * Treasure_xianjiaSecret配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Treasure_xianjiaSecret_Bean{
    /**
     * 编号，主键type*100+num
     */
    private final int id;
    /**
     * 编号，主键type*100+num
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 小目标
大目标
     */
    private final int type;
    /**
     * 小目标
大目标
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 轮数
     */
    private final int round;
    /**
     * 轮数
     * @return
     */
    public final int getRound(){
        return round;
    }
    /**
     * 需求寻宝次数
     */
    private final int time;
    /**
     * 需求寻宝次数
     * @return
     */
    public final int getTime(){
        return time;
    }
    /**
     * item_num_bind_occ
bind 0未绑定，1绑定
occ 0男1女9通用
     */
    private final ReadIntegerArrayEs reward;
    /**
     * item_num_bind_occ
bind 0未绑定，1绑定
occ 0男1女9通用
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 权重概率(万分比）；抽走之后剩余之和来重新相除来计算概率
     */
    private final int probability;
    /**
     * 权重概率(万分比）；抽走之后剩余之和来重新相除来计算概率
     * @return
     */
    public final int getProbability(){
        return probability;
    }
    /**
     * 是否广播
0不广播
1广播
     */
    private final int ifRadio;
    /**
     * 是否广播
0不广播
1广播
     * @return
     */
    public final int getIfRadio(){
        return ifRadio;
    }

    public Cfg_Treasure_xianjiaSecret_Bean(int id,int type,int round,int time,String rewardStr,int probability,int ifRadio){
        this.id = id;
        this.type = type;
        this.round = round;
        this.time = time;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.probability = probability;
        this.ifRadio = ifRadio;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("round:").append(round).append(";");
        str.append("time:").append(time).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("probability:").append(probability).append(";");
        str.append("ifRadio:").append(ifRadio).append(";");
        return str.toString();
    }
}
