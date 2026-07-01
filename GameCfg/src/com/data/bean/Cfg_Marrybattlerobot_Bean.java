/**
 * Auto generated, do not edit it
 *
 * marrybattlerobot配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Marrybattlerobot_Bean{
    /**
     * 机器人ID
     */
    private final int robotID;
    /**
     * 机器人ID
     * @return
     */
    public final int getRobotID(){
        return robotID;
    }
    /**
     * 名字
     */
    private final String name;
    /**
     * 名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 组ID，相同组的机器人为同一场次
     */
    private final int group_ID;
    /**
     * 组ID，相同组的机器人为同一场次
     * @return
     */
    public final int getGroup_ID(){
        return group_ID;
    }
    /**
     * 职业
     */
    private final int career;
    /**
     * 职业
     * @return
     */
    public final int getCareer(){
        return career;
    }
    /**
     * 属性类型_数值(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 属性类型_数值(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 技能_等级(@;@_@)
     */
    private final ReadIntegerArrayEs skill;
    /**
     * 技能_等级(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getSkill(){
        return skill;
    }
    /**
     * 武器时装ID
     */
    private final int weaponsEquipId;
    /**
     * 武器时装ID
     * @return
     */
    public final int getWeaponsEquipId(){
        return weaponsEquipId;
    }
    /**
     * 衣服时装ID
     */
    private final int clotheEquipId;
    /**
     * 衣服时装ID
     * @return
     */
    public final int getClotheEquipId(){
        return clotheEquipId;
    }

    public Cfg_Marrybattlerobot_Bean(int robotID,String name,int group_ID,int career,String attributeStr,String skillStr,int weaponsEquipId,int clotheEquipId){
        this.robotID = robotID;
        this.name = name;
        this.group_ID = group_ID;
        this.career = career;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.skill = new ReadIntegerArrayEs(skillStr,"}",",");
        this.weaponsEquipId = weaponsEquipId;
        this.clotheEquipId = clotheEquipId;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("robotID:").append(robotID).append(";");
        str.append("name:").append(name).append(";");
        str.append("group_ID:").append(group_ID).append(";");
        str.append("career:").append(career).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("weaponsEquipId:").append(weaponsEquipId).append(";");
        str.append("clotheEquipId:").append(clotheEquipId).append(";");
        return str.toString();
    }
}
