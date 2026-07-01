/**
 * Auto generated, do not edit it
 *
 * new_sever_luckcard配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_New_sever_luckcard_Bean{
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
     * 对应FunctionVariable条件（可配置多条，多条则代表需要同时满足）
     */
    private final ReadIntegerArrayEs condition;
    /**
     * 对应FunctionVariable条件（可配置多条，多条则代表需要同时满足）
     * @return
     */
    public final ReadIntegerArrayEs getCondition(){
        return condition;
    }
    /**
     * 对应奖励的幸运值
     */
    private final int reward;
    /**
     * 对应奖励的幸运值
     * @return
     */
    public final int getReward(){
        return reward;
    }

    public Cfg_New_sever_luckcard_Bean(int id,String conditionStr,int reward){
        this.id = id;
        this.condition = new ReadIntegerArrayEs(conditionStr,"}",",");
        this.reward = reward;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
