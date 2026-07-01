/**
 * Auto generated, do not edit it
 *
 * new_sever_exchange配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_New_sever_exchange_Bean{
    /**
     * key用于标识
     */
    private final int id;
    /**
     * key用于标识
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 兑换道具
     */
    private final ReadIntegerArrayEs item;
    /**
     * 兑换道具
     * @return
     */
    public final ReadIntegerArrayEs getItem(){
        return item;
    }
    /**
     * 每日兑换道具的限制次数，每日零点清除，不配表示无限
     */
    private final int limit_time;
    /**
     * 每日兑换道具的限制次数，每日零点清除，不配表示无限
     * @return
     */
    public final int getLimit_time(){
        return limit_time;
    }
    /**
     * 给与的奖励
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 给与的奖励
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_New_sever_exchange_Bean(int id,String itemStr,int limit_time,String rewardStr){
        this.id = id;
        this.item = new ReadIntegerArrayEs(itemStr,"}",",");
        this.limit_time = limit_time;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("item:").append(item).append(";");
        str.append("limit_time:").append(limit_time).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
