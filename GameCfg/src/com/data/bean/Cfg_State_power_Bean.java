/**
 * Auto generated, do not edit it
 *
 * state_power配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_State_power_Bean{
    /**
     * 表示境界等级
     */
    private final int id;
    /**
     * 表示境界等级
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 境界名字
     */
    private final String name;
    /**
     * 境界名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 默认为0表示小境界，突破
1表示大境界需要渡劫
     */
    private final int specail;
    /**
     * 默认为0表示小境界，突破
1表示大境界需要渡劫
     * @return
     */
    public final int getSpecail(){
        return specail;
    }
    /**
     * 需要渡劫的时候进入的特殊场景
副本ID，坐标点，
地图_出入坐标点_位面坐标点_位面地图
     */
    private final ReadIntegerArray specail_clone;
    /**
     * 需要渡劫的时候进入的特殊场景
副本ID，坐标点，
地图_出入坐标点_位面坐标点_位面地图
     * @return
     */
    public final ReadIntegerArray getSpecail_clone(){
        return specail_clone;
    }
    /**
     * 渡劫后给与玩家的飞剑化形
     */
    private final int fly_sword_modele;
    /**
     * 渡劫后给与玩家的飞剑化形
     * @return
     */
    public final int getFly_sword_modele(){
        return fly_sword_modele;
    }
    /**
     * 激活境界获得的奖励道具和数量(@;@_@)
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 激活境界获得的奖励道具和数量(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 属性(@;@_@)
     */
    private final ReadIntegerArrayEs Value;
    /**
     * 属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getValue(){
        return Value;
    }
    /**
     * 技能Icon
     */
    private final int skill_icon;
    /**
     * 技能Icon
     * @return
     */
    public final int getSkill_icon(){
        return skill_icon;
    }
    /**
     * type_参数
type1=固定值
type2=生命上限百分比(万分比）
type3=固定值+百分比
使用XP技能时回复生命值的技能效果字段
客户端描述配置在global 4830
     */
    private final ReadIntegerArray skill_effect;
    /**
     * type_参数
type1=固定值
type2=生命上限百分比(万分比）
type3=固定值+百分比
使用XP技能时回复生命值的技能效果字段
客户端描述配置在global 4830
     * @return
     */
    public final ReadIntegerArray getSkill_effect(){
        return skill_effect;
    }

    public Cfg_State_power_Bean(int id,String name,int specail,String specail_cloneStr,int fly_sword_modele,String rewardStr,String ValueStr,int skill_icon,String skill_effectStr){
        this.id = id;
        this.name = name;
        this.specail = specail;
        this.specail_clone = new ReadIntegerArray(specail_cloneStr,",");
        this.fly_sword_modele = fly_sword_modele;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.Value = new ReadIntegerArrayEs(ValueStr,"}",",");
        this.skill_icon = skill_icon;
        this.skill_effect = new ReadIntegerArray(skill_effectStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("specail:").append(specail).append(";");
        str.append("specail_clone:").append(specail_clone).append(";");
        str.append("fly_sword_modele:").append(fly_sword_modele).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("Value:").append(Value).append(";");
        str.append("skill_icon:").append(skill_icon).append(";");
        str.append("skill_effect:").append(skill_effect).append(";");
        return str.toString();
    }
}
