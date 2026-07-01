/**
 * Auto generated, do not edit it
 *
 * recharge_daily_cangzhenge配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Recharge_daily_cangzhenge_Bean{
    /**
     * ID
     */
    private final int ID;
    /**
     * ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 第几轮
     */
    private final int times;
    /**
     * 第几轮
     * @return
     */
    public final int getTimes(){
        return times;
    }
    /**
     * item_num_bind_occ
bind 0未绑定1绑定
occ 0男1女9通用
     */
    private final ReadIntegerArrayEs reward;
    /**
     * item_num_bind_occ
bind 0未绑定1绑定
occ 0男1女9通用
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 几率（和为10W）
     */
    private final int probability;
    /**
     * 几率（和为10W）
     * @return
     */
    public final int getProbability(){
        return probability;
    }
    /**
     * 是否为最后一轮（0，不是；1，是）
     */
    private final int end;
    /**
     * 是否为最后一轮（0，不是；1，是）
     * @return
     */
    public final int getEnd(){
        return end;
    }
    /**
     * 是否进入大奖的记录
     */
    private final int record;
    /**
     * 是否进入大奖的记录
     * @return
     */
    public final int getRecord(){
        return record;
    }

    public Cfg_Recharge_daily_cangzhenge_Bean(int ID,int times,String rewardStr,int probability,int end,int record){
        this.ID = ID;
        this.times = times;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.probability = probability;
        this.end = end;
        this.record = record;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("times:").append(times).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("probability:").append(probability).append(";");
        str.append("end:").append(end).append(";");
        str.append("record:").append(record).append(";");
        return str.toString();
    }
}
