/**
 * Auto generated, do not edit it
 *
 * marry_activity_bless配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Marry_activity_bless_Bean{
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
     * 类型
1：击杀首领类
0：副本类
     */
    private final int type;
    /**
     * 类型
1：击杀首领类
0：副本类
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 子类型
type=1：对应monster表的BOSS_type字段
type=0：对应clone_map的主键
     */
    private final int subType;
    /**
     * 子类型
type=1：对应monster表的BOSS_type字段
type=0：对应clone_map的主键
     * @return
     */
    public final int getSubType(){
        return subType;
    }
    /**
     * 完成一次所得奖励
（击杀BOSS按照击杀一次计算，通关副本按照通关一次计算）
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 完成一次所得奖励
（击杀BOSS按照击杀一次计算，通关副本按照通关一次计算）
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_Marry_activity_bless_Bean(int id,int type,int subType,String rewardStr){
        this.id = id;
        this.type = type;
        this.subType = subType;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("subType:").append(subType).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
