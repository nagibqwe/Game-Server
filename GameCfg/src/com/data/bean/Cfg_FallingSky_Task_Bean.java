/**
 * Auto generated, do not edit it
 *
 * FallingSky_Task配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_FallingSky_Task_Bean{
    /**
     * 
     */
    private final int id;
    /**
     * 
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 
     */
    private final int group;
    /**
     * 
     * @return
     */
    public final int getGroup(){
        return group;
    }
    /**
     * 1：每日任务（每天5点刷新）
2：阶段任务（每周刷一次，活动开启时每7天刷新一次
3：降妖除魔（一轮活动刷新一次）
     */
    private final int type;
    /**
     * 1：每日任务（每天5点刷新）
2：阶段任务（每周刷一次，活动开启时每7天刷新一次
3：降妖除魔（一轮活动刷新一次）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 对应FunctionVariable主键
     */
    private final ReadIntegerArray condition;
    /**
     * 对应FunctionVariable主键
     * @return
     */
    public final ReadIntegerArray getCondition(){
        return condition;
    }
    /**
     * 任务奖励
（客户端直接去第二个数字做为界面显示）
     */
    private final ReadIntegerArrayEs Reward;
    /**
     * 任务奖励
（客户端直接去第二个数字做为界面显示）
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return Reward;
    }

    public Cfg_FallingSky_Task_Bean(int id,int group,int type,String conditionStr,String RewardStr){
        this.id = id;
        this.group = group;
        this.type = type;
        this.condition = new ReadIntegerArray(conditionStr,",");
        this.Reward = new ReadIntegerArrayEs(RewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("group:").append(group).append(";");
        str.append("type:").append(type).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("Reward:").append(Reward).append(";");
        return str.toString();
    }
}
