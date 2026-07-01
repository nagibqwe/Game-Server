/**
 * Auto generated, do not edit it
 *
 * level_reward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Level_reward_Bean{
    /**
     * 等级要求
     */
    private final int q_level;
    /**
     * 等级要求
     * @return
     */
    public final int getQ_level(){
        return q_level;
    }
    /**
     * 装备,ID_数量_是否绑定（0否 1是）_职业(@;@_@)
Occ:0男；1女；9通用；0，1只对应职业发，通用都会发
     */
    private final ReadIntegerArrayEs q_reward;
    /**
     * 装备,ID_数量_是否绑定（0否 1是）_职业(@;@_@)
Occ:0男；1女；9通用；0，1只对应职业发，通用都会发
     * @return
     */
    public final ReadIntegerArrayEs getQ_reward(){
        return q_reward;
    }
    /**
     * VIP额外奖励列表
装备,ID_数量_是否绑定（0否 1是）_职业(@;@_@)
Occ:0男；1女；9通用；0，1只对应职业发，通用都会发
     */
    private final ReadIntegerArrayEs q_reward_vip;
    /**
     * VIP额外奖励列表
装备,ID_数量_是否绑定（0否 1是）_职业(@;@_@)
Occ:0男；1女；9通用；0，1只对应职业发，通用都会发
     * @return
     */
    public final ReadIntegerArrayEs getQ_reward_vip(){
        return q_reward_vip;
    }
    /**
     * 对应领取VIP额外奖励所需的VIP等级
     */
    private final int vipLimit;
    /**
     * 对应领取VIP额外奖励所需的VIP等级
     * @return
     */
    public final int getVipLimit(){
        return vipLimit;
    }
    /**
     * 限制数据，-1为不限，0 为不可领，大于0为可领数量
     */
    private final int limitValue;
    /**
     * 限制数据，-1为不限，0 为不可领，大于0为可领数量
     * @return
     */
    public final int getLimitValue(){
        return limitValue;
    }
    /**
     * 是否显示气泡0否1是
     */
    private final int paoPao;
    /**
     * 是否显示气泡0否1是
     * @return
     */
    public final int getPaoPao(){
        return paoPao;
    }

    public Cfg_Level_reward_Bean(int q_level,String q_rewardStr,String q_reward_vipStr,int vipLimit,int limitValue,int paoPao){
        this.q_level = q_level;
        this.q_reward = new ReadIntegerArrayEs(q_rewardStr,"}",",");
        this.q_reward_vip = new ReadIntegerArrayEs(q_reward_vipStr,"}",",");
        this.vipLimit = vipLimit;
        this.limitValue = limitValue;
        this.paoPao = paoPao;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("q_level:").append(q_level).append(";");
        str.append("q_reward:").append(q_reward).append(";");
        str.append("q_reward_vip:").append(q_reward_vip).append(";");
        str.append("vipLimit:").append(vipLimit).append(";");
        str.append("limitValue:").append(limitValue).append(";");
        str.append("paoPao:").append(paoPao).append(";");
        return str.toString();
    }
}
