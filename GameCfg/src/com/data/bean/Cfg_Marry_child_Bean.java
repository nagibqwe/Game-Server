/**
 * Auto generated, do not edit it
 *
 * marry_child配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Marry_child_Bean{
    /**
     * 列表ID，不能随意
     */
    private final int Id;
    /**
     * 列表ID，不能随意
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 激活条件
1、或(两个达成一个即可）
2，且（两个条件都需要达成）
     */
    private final int activation;
    /**
     * 激活条件
1、或(两个达成一个即可）
2，且（两个条件都需要达成）
     * @return
     */
    public final int getActivation(){
        return activation;
    }
    /**
     * 道具激活，对应item中ID，填0为条件激活(@;@_@)
     */
    private final ReadIntegerArrayEs itemCondition;
    /**
     * 道具激活，对应item中ID，填0为条件激活(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getItemCondition(){
        return itemCondition;
    }
    /**
     * 条件激活对应marry_lock 等级，填0为道具激活
     */
    private final int condition;
    /**
     * 条件激活对应marry_lock 等级，填0为道具激活
     * @return
     */
    public final int getCondition(){
        return condition;
    }
    /**
     * 仙娃名称
     */
    private final String childName;
    /**
     * 仙娃名称
     * @return
     */
    public final String getChildName(){
        return childName;
    }
    /**
     * 技能ID_解锁等级
技能ID(对应skill技能主键）
解锁等级（对应仙娃等级）
     */
    private final ReadIntegerArrayEs skillId;
    /**
     * 技能ID_解锁等级
技能ID(对应skill技能主键）
解锁等级（对应仙娃等级）
     * @return
     */
    public final ReadIntegerArrayEs getSkillId(){
        return skillId;
    }

    public Cfg_Marry_child_Bean(int Id,int activation,String itemConditionStr,int condition,String childName,String skillIdStr){
        this.Id = Id;
        this.activation = activation;
        this.itemCondition = new ReadIntegerArrayEs(itemConditionStr,"}",",");
        this.condition = condition;
        this.childName = childName;
        this.skillId = new ReadIntegerArrayEs(skillIdStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("activation:").append(activation).append(";");
        str.append("itemCondition:").append(itemCondition).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("childName:").append(childName).append(";");
        str.append("skillId:").append(skillId).append(";");
        return str.toString();
    }
}
