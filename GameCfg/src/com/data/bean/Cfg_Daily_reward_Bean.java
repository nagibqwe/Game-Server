/**
 * Auto generated, do not edit it
 *
 * daily_reward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Daily_reward_Bean{
    /**
     * 排序
     */
    private final int id;
    /**
     * 排序
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 开启需要积分
     */
    private final int q_needintegral;
    /**
     * 开启需要积分
     * @return
     */
    public final int getQ_needintegral(){
        return q_needintegral;
    }
    /**
     * 奖励物品1(物品ID_数量_绑定)(@;@_@)
     */
    private final ReadIntegerArrayEs q_reward_item;
    /**
     * 奖励物品1(物品ID_数量_绑定)(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getQ_reward_item(){
        return q_reward_item;
    }

    public Cfg_Daily_reward_Bean(int id,int q_needintegral,String q_reward_itemStr){
        this.id = id;
        this.q_needintegral = q_needintegral;
        this.q_reward_item = new ReadIntegerArrayEs(q_reward_itemStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("q_needintegral:").append(q_needintegral).append(";");
        str.append("q_reward_item:").append(q_reward_item).append(";");
        return str.toString();
    }
}
