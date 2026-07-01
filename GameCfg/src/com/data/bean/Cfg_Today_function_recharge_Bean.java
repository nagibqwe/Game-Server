/**
 * Auto generated, do not edit it
 *
 * today_function_recharge配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Today_function_recharge_Bean{
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
     * 充值奖励（除了充值灵玉灵玉）
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 充值奖励（除了充值灵玉灵玉）
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 开启的天数（闭区间）
     */
    private final ReadIntegerArray open_day;
    /**
     * 开启的天数（闭区间）
     * @return
     */
    public final ReadIntegerArray getOpen_day(){
        return open_day;
    }
    /**
     * 对应rechargeItem表的ID
     */
    private final int rechargeID;
    /**
     * 对应rechargeItem表的ID
     * @return
     */
    public final int getRechargeID(){
        return rechargeID;
    }
    /**
     * 折扣数
     */
    private final int discount;
    /**
     * 折扣数
     * @return
     */
    public final int getDiscount(){
        return discount;
    }

    public Cfg_Today_function_recharge_Bean(int id,String rewardStr,String open_dayStr,int rechargeID,int discount){
        this.id = id;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.open_day = new ReadIntegerArray(open_dayStr,",");
        this.rechargeID = rechargeID;
        this.discount = discount;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("open_day:").append(open_day).append(";");
        str.append("rechargeID:").append(rechargeID).append(";");
        str.append("discount:").append(discount).append(";");
        return str.toString();
    }
}
