/**
 * Auto generated, do not edit it
 *
 * task_branch配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadLongArrayEs; 
	
public class Cfg_Task_branch_Bean{
    /**
     * 任务编号
     */
    private final int branchId;
    /**
     * 任务编号
     * @return
     */
    public final int getBranchId(){
        return branchId;
    }
    /**
     * 任务名称
     */
    private final String name;
    /**
     * 任务名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 任务类型名称
     */
    private final String type_name;
    /**
     * 任务类型名称
     * @return
     */
    public final String getType_name(){
        return type_name;
    }
    /**
     * 开服天数控制类型(ClientIgnore)(0普通1限时）
     */
    private final int subtype;
    /**
     * 开服天数控制类型(ClientIgnore)(0普通1限时）
     * @return
     */
    public final int getSubtype(){
        return subtype;
    }
    /**
     * 领取条件：类型_值(@;@_@)
（类型读FunctionVariable表：1为等级，2为任务,160为开服时间,等级)
     */
    private final ReadIntegerArrayEs conditions_value;
    /**
     * 领取条件：类型_值(@;@_@)
（类型读FunctionVariable表：1为等级，2为任务,160为开服时间,等级)
     * @return
     */
    public final ReadIntegerArrayEs getConditions_value(){
        return conditions_value;
    }
    /**
     * 任务类型（6功能操作）
     */
    private final int conditions_type;
    /**
     * 任务类型（6功能操作）
     * @return
     */
    public final int getConditions_type(){
        return conditions_type;
    }
    /**
     * 任务需求具体值X_X具体参看支线任务文档(@;@_@)
     */
    private final ReadIntegerArrayEs demand_value;
    /**
     * 任务需求具体值X_X具体参看支线任务文档(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getDemand_value(){
        return demand_value;
    }
    /**
     * 使用道具的道具ID
     */
    private final int itemID;
    /**
     * 使用道具的道具ID
     * @return
     */
    public final int getItemID(){
        return itemID;
    }
    /**
     * 任务奖励(物品_数量；物品_数量；)[@;@_@]
     */
    private final ReadLongArrayEs task_reward;
    /**
     * 任务奖励(物品_数量；物品_数量；)[@;@_@]
     * @return
     */
    public final ReadLongArrayEs getTask_reward(){
        return task_reward;
    }
    /**
     * 面板参数ID
     */
    private final int back_group_id;
    /**
     * 面板参数ID
     * @return
     */
    public final int getBack_group_id(){
        return back_group_id;
    }
    /**
     * 开服天数控制(ClientIgnore)
     */
    private final int ifGono;
    /**
     * 开服天数控制(ClientIgnore)
     * @return
     */
    public final int getIfGono(){
        return ifGono;
    }

    public Cfg_Task_branch_Bean(int branchId,String name,String type_name,int subtype,String conditions_valueStr,int conditions_type,String demand_valueStr,int itemID,String task_rewardStr,int back_group_id,int ifGono){
        this.branchId = branchId;
        this.name = name;
        this.type_name = type_name;
        this.subtype = subtype;
        this.conditions_value = new ReadIntegerArrayEs(conditions_valueStr,"}",",");
        this.conditions_type = conditions_type;
        this.demand_value = new ReadIntegerArrayEs(demand_valueStr,"}",",");
        this.itemID = itemID;
        this.task_reward = new ReadLongArrayEs(task_rewardStr,"}",",");
        this.back_group_id = back_group_id;
        this.ifGono = ifGono;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("branchId:").append(branchId).append(";");
        str.append("name:").append(name).append(";");
        str.append("type_name:").append(type_name).append(";");
        str.append("subtype:").append(subtype).append(";");
        str.append("conditions_value:").append(conditions_value).append(";");
        str.append("conditions_type:").append(conditions_type).append(";");
        str.append("demand_value:").append(demand_value).append(";");
        str.append("itemID:").append(itemID).append(";");
        str.append("task_reward:").append(task_reward).append(";");
        str.append("back_group_id:").append(back_group_id).append(";");
        str.append("ifGono:").append(ifGono).append(";");
        return str.toString();
    }
}
