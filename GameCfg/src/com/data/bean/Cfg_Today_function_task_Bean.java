/**
 * Auto generated, do not edit it
 *
 * today_function_task配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Today_function_task_Bean{
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
     * 任务类型（1，每日任务（每天刷新，每天都可以领奖)2.唯一任务（期间内不会刷新，只能领取一次奖励)
     */
    private final int type;
    /**
     * 任务类型（1，每日任务（每天刷新，每天都可以领奖)2.唯一任务（期间内不会刷新，只能领取一次奖励)
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 任务描述
     */
    private final String des;
    /**
     * 任务描述
     * @return
     */
    public final String getDes(){
        return des;
    }
    /**
     * 任务条件
     */
    private final ReadIntegerArray Variable;
    /**
     * 任务条件
     * @return
     */
    public final ReadIntegerArray getVariable(){
        return Variable;
    }
    /**
     * 任务奖励
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 任务奖励
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 对应的核心玩法功能ID
     */
    private final ReadIntegerArray today_functionID;
    /**
     * 对应的核心玩法功能ID
     * @return
     */
    public final ReadIntegerArray getToday_functionID(){
        return today_functionID;
    }
    /**
     * 跳转的functionID
     */
    private final int functionID;
    /**
     * 跳转的functionID
     * @return
     */
    public final int getFunctionID(){
        return functionID;
    }
    /**
     * 跳转参数
     */
    private final int parm;
    /**
     * 跳转参数
     * @return
     */
    public final int getParm(){
        return parm;
    }
    /**
     * 在开启时是否检查角色的状态（0，不检查；1，检查）
     */
    private final int if_after_open;
    /**
     * 在开启时是否检查角色的状态（0，不检查；1，检查）
     * @return
     */
    public final int getIf_after_open(){
        return if_after_open;
    }
    /**
     * 任务展示次数，根据核心玩法功能开启次数判断
     */
    private final int show_count;
    /**
     * 任务展示次数，根据核心玩法功能开启次数判断
     * @return
     */
    public final int getShow_count(){
        return show_count;
    }

    public Cfg_Today_function_task_Bean(int id,int type,String des,String VariableStr,String rewardStr,String today_functionIDStr,int functionID,int parm,int if_after_open,int show_count){
        this.id = id;
        this.type = type;
        this.des = des;
        this.Variable = new ReadIntegerArray(VariableStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.today_functionID = new ReadIntegerArray(today_functionIDStr,",");
        this.functionID = functionID;
        this.parm = parm;
        this.if_after_open = if_after_open;
        this.show_count = show_count;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("des:").append(des).append(";");
        str.append("Variable:").append(Variable).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("today_functionID:").append(today_functionID).append(";");
        str.append("functionID:").append(functionID).append(";");
        str.append("parm:").append(parm).append(";");
        str.append("if_after_open:").append(if_after_open).append(";");
        str.append("show_count:").append(show_count).append(";");
        return str.toString();
    }
}
