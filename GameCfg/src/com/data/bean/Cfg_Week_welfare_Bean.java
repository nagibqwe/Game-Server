/**
 * Auto generated, do not edit it
 *
 * week_welfare配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Week_welfare_Bean{
    /**
     * 任务ID
     */
    private final int id;
    /**
     * 任务ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 任务条件,对应FunctionVariable
     */
    private final ReadIntegerArray condition;
    /**
     * 任务条件,对应FunctionVariable
     * @return
     */
    public final ReadIntegerArray getCondition(){
        return condition;
    }
    /**
     * 功能Id
     */
    private final int functionId;
    /**
     * 功能Id
     * @return
     */
    public final int getFunctionId(){
        return functionId;
    }
    /**
     * 奖励
item_num_bind_occ
bind:0未绑定1绑定
occ：0男1女9通用
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励
item_num_bind_occ
bind:0未绑定1绑定
occ：0男1女9通用
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_Week_welfare_Bean(int id,String conditionStr,int functionId,String rewardStr){
        this.id = id;
        this.condition = new ReadIntegerArray(conditionStr,",");
        this.functionId = functionId;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("functionId:").append(functionId).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
