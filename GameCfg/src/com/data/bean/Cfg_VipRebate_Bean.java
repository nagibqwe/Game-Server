/**
 * Auto generated, do not edit it
 *
 * VipRebate配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_VipRebate_Bean{
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
     * 条件在FunctionVariable配置
     */
    private final ReadIntegerArray VariableId;
    /**
     * 条件在FunctionVariable配置
     * @return
     */
    public final ReadIntegerArray getVariableId(){
        return VariableId;
    }
    /**
     * 任务处于第几阶段:0.第一阶段1.第二阶段2.第三阶段3.第四阶段
     */
    private final int StageType;
    /**
     * 任务处于第几阶段:0.第一阶段1.第二阶段2.第三阶段3.第四阶段
     * @return
     */
    public final int getStageType(){
        return StageType;
    }
    /**
     * 是否继承前一页已完成信息：0.不继承，继承则为继承任务Id
     */
    private final int TaskInherit;
    /**
     * 是否继承前一页已完成信息：0.不继承，继承则为继承任务Id
     * @return
     */
    public final int getTaskInherit(){
        return TaskInherit;
    }

    public Cfg_VipRebate_Bean(int id,String VariableIdStr,int StageType,int TaskInherit){
        this.id = id;
        this.VariableId = new ReadIntegerArray(VariableIdStr,",");
        this.StageType = StageType;
        this.TaskInherit = TaskInherit;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("VariableId:").append(VariableId).append(";");
        str.append("StageType:").append(StageType).append(";");
        str.append("TaskInherit:").append(TaskInherit).append(";");
        return str.toString();
    }
}
