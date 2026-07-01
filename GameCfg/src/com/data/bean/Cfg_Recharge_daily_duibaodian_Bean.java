/**
 * Auto generated, do not edit it
 *
 * recharge_daily_duibaodian配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Recharge_daily_duibaodian_Bean{
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
     * 兑换需要的物品_id
     */
    private final ReadIntegerArray need;
    /**
     * 兑换需要的物品_id
     * @return
     */
    public final ReadIntegerArray getNeed(){
        return need;
    }
    /**
     * 每次的兑换次数
     */
    private final int exchange_limit;
    /**
     * 每次的兑换次数
     * @return
     */
    public final int getExchange_limit(){
        return exchange_limit;
    }
    /**
     * 是否为最后一轮（0，不是；1，是）
     */
    private final int if_end;
    /**
     * 是否为最后一轮（0，不是；1，是）
     * @return
     */
    public final int getIf_end(){
        return if_end;
    }

    public Cfg_Recharge_daily_duibaodian_Bean(int ID,int times,String rewardStr,String needStr,int exchange_limit,int if_end){
        this.ID = ID;
        this.times = times;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.need = new ReadIntegerArray(needStr,",");
        this.exchange_limit = exchange_limit;
        this.if_end = if_end;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("times:").append(times).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("need:").append(need).append(";");
        str.append("exchange_limit:").append(exchange_limit).append(";");
        str.append("if_end:").append(if_end).append(";");
        return str.toString();
    }
}
