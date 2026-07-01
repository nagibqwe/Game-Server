/**
 * Auto generated, do not edit it
 *
 * social_house_task配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadLongArrayEs; 
	
public class Cfg_Social_house_task_Bean{
    /**
     * 任务编号
     */
    private final int Id;
    /**
     * 任务编号
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 任务的分类：
1=拜访 count
2=送礼 count
3=购买商城家具 count
4=人气 value
5=装饰度 value
6=收集家具套装 suit
7=收集家具类型 type_count
8=聚宝盆 level

     */
    private final int type;
    /**
     * 任务的分类：
1=拜访 count
2=送礼 count
3=购买商城家具 count
4=人气 value
5=装饰度 value
6=收集家具套装 suit
7=收集家具类型 type_count
8=聚宝盆 level

     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 任务刷新：
0=每日
1=永久
     */
    private final int daily;
    /**
     * 任务刷新：
0=每日
1=永久
     * @return
     */
    public final int getDaily(){
        return daily;
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
     * 任务领取条件
     */
    private final ReadIntegerArrayEs demand_value;
    /**
     * 任务领取条件
     * @return
     */
    public final ReadIntegerArrayEs getDemand_value(){
        return demand_value;
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
     * 根据任务分类
     */
    private final ReadIntegerArrayEs conditions_value;
    /**
     * 根据任务分类
     * @return
     */
    public final ReadIntegerArrayEs getConditions_value(){
        return conditions_value;
    }

    public Cfg_Social_house_task_Bean(int Id,int type,int daily,String name,String demand_valueStr,String task_rewardStr,String conditions_valueStr){
        this.Id = Id;
        this.type = type;
        this.daily = daily;
        this.name = name;
        this.demand_value = new ReadIntegerArrayEs(demand_valueStr,"}",",");
        this.task_reward = new ReadLongArrayEs(task_rewardStr,"}",",");
        this.conditions_value = new ReadIntegerArrayEs(conditions_valueStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("type:").append(type).append(";");
        str.append("daily:").append(daily).append(";");
        str.append("name:").append(name).append(";");
        str.append("demand_value:").append(demand_value).append(";");
        str.append("task_reward:").append(task_reward).append(";");
        str.append("conditions_value:").append(conditions_value).append(";");
        return str.toString();
    }
}
