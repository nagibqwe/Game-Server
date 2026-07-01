/**
 * Auto generated, do not edit it
 *
 * world_bonfire配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_World_bonfire_Bean{
    /**
     * 等级
     */
    private final int id;
    /**
     * 等级
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 升级所需经验
     */
    private final int level_exp;
    /**
     * 升级所需经验
     * @return
     */
    public final int getLevel_exp(){
        return level_exp;
    }
    /**
     * 本级额外经验加成（每跳x%)
     */
    private final int exp_addition;
    /**
     * 本级额外经验加成（每跳x%)
     * @return
     */
    public final int getExp_addition(){
        return exp_addition;
    }
    /**
     * 本级添柴消耗（物品ID_数量）
     */
    private final ReadIntegerArray wood_cost;
    /**
     * 本级添柴消耗（物品ID_数量）
     * @return
     */
    public final ReadIntegerArray getWood_cost(){
        return wood_cost;
    }
    /**
     * 本级添柴增加的经验
     */
    private final int wood_exp;
    /**
     * 本级添柴增加的经验
     * @return
     */
    public final int getWood_exp(){
        return wood_exp;
    }
    /**
     * 添柴召唤的采集物
     */
    private final int add_gather;
    /**
     * 添柴召唤的采集物
     * @return
     */
    public final int getAdd_gather(){
        return add_gather;
    }
    /**
     * 添柴召唤的采集物数量
     */
    private final int add_gather_num;
    /**
     * 添柴召唤的采集物数量
     * @return
     */
    public final int getAdd_gather_num(){
        return add_gather_num;
    }
    /**
     * 采集物坐标
     */
    private final ReadIntegerArrayEs gather_location;
    /**
     * 采集物坐标
     * @return
     */
    public final ReadIntegerArrayEs getGather_location(){
        return gather_location;
    }

    public Cfg_World_bonfire_Bean(int id,int level_exp,int exp_addition,String wood_costStr,int wood_exp,int add_gather,int add_gather_num,String gather_locationStr){
        this.id = id;
        this.level_exp = level_exp;
        this.exp_addition = exp_addition;
        this.wood_cost = new ReadIntegerArray(wood_costStr,",");
        this.wood_exp = wood_exp;
        this.add_gather = add_gather;
        this.add_gather_num = add_gather_num;
        this.gather_location = new ReadIntegerArrayEs(gather_locationStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("level_exp:").append(level_exp).append(";");
        str.append("exp_addition:").append(exp_addition).append(";");
        str.append("wood_cost:").append(wood_cost).append(";");
        str.append("wood_exp:").append(wood_exp).append(";");
        str.append("add_gather:").append(add_gather).append(";");
        str.append("add_gather_num:").append(add_gather_num).append(";");
        str.append("gather_location:").append(gather_location).append(";");
        return str.toString();
    }
}
