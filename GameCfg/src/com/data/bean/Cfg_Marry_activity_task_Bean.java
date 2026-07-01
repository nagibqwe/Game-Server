/**
 * Auto generated, do not edit it
 *
 * marry_activity_task配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Marry_activity_task_Bean{
    /**
     * 排序id
     */
    private final int id;
    /**
     * 排序id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 任务类型
1：子任务
0：总任务
     */
    private final int type;
    /**
     * 任务类型
1：子任务
0：总任务
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 达成的成就条件(@_@)
条件都需要定义在functionVariable中
     */
    private final ReadIntegerArray condition;
    /**
     * 达成的成就条件(@_@)
条件都需要定义在functionVariable中
     * @return
     */
    public final ReadIntegerArray getCondition(){
        return condition;
    }
    /**
     * 完成该任务后获得的进度点
该字段总任务代表所需进度点
     */
    private final int rate;
    /**
     * 完成该任务后获得的进度点
该字段总任务代表所需进度点
     * @return
     */
    public final int getRate(){
        return rate;
    }
    /**
     * 完成任务所得奖励
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 完成任务所得奖励
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_Marry_activity_task_Bean(int id,int type,String conditionStr,int rate,String rewardStr){
        this.id = id;
        this.type = type;
        this.condition = new ReadIntegerArray(conditionStr,",");
        this.rate = rate;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("rate:").append(rate).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
