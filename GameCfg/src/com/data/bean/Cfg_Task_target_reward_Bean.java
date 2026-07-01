/**
 * Auto generated, do not edit it
 *
 * task_target_reward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Task_target_reward_Bean{
    /**
     * 阶段ID
     */
    private final int id;
    /**
     * 阶段ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 需要的目标值
     */
    private final int need_num;
    /**
     * 需要的目标值
     * @return
     */
    public final int getNeed_num(){
        return need_num;
    }
    /**
     * 奖励
item_num_bind_occ
bind:0未绑定，1绑定
occ：0男1女9通用(0,1代表当前物品只发给对应的职业，9代表当前物品所有职业都发）
第一个物品奖励作为目标阶段主奖励
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励
item_num_bind_occ
bind:0未绑定，1绑定
occ：0男1女9通用(0,1代表当前物品只发给对应的职业，9代表当前物品所有职业都发）
第一个物品奖励作为目标阶段主奖励
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 是否是循环阶段（0，不是，1是）
     */
    private final int if_loop;
    /**
     * 是否是循环阶段（0，不是，1是）
     * @return
     */
    public final int getIf_loop(){
        return if_loop;
    }

    public Cfg_Task_target_reward_Bean(int id,int need_num,String rewardStr,int if_loop){
        this.id = id;
        this.need_num = need_num;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.if_loop = if_loop;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("need_num:").append(need_num).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("if_loop:").append(if_loop).append(";");
        return str.toString();
    }
}
