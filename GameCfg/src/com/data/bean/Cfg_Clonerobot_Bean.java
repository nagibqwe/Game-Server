/**
 * Auto generated, do not edit it
 *
 * clonerobot配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Clonerobot_Bean{
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
     * 机器人等级
     */
    private final int level;
    /**
     * 机器人等级
     * @return
     */
    public final int getLevel(){
        return level;
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
    /**
     * 阵道ID
     */
    private final int zhendaoId;
    /**
     * 阵道ID
     * @return
     */
    public final int getZhendaoId(){
        return zhendaoId;
    }
    /**
     * 光环ID
     */
    private final int guanghuanId;
    /**
     * 光环ID
     * @return
     */
    public final int getGuanghuanId(){
        return guanghuanId;
    }
    /**
     * 称号ID
     */
    private final int title;
    /**
     * 称号ID
     * @return
     */
    public final int getTitle(){
        return title;
    }

    public Cfg_Clonerobot_Bean(int robotID,int level,int career,String name,String attributeStr,String skillStr,int weaponsEquipId,int clotheEquipId,int zhendaoId,int guanghuanId,int title){
        this.robotID = robotID;
        this.level = level;
        this.career = career;
        this.name = name;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.skill = new ReadIntegerArrayEs(skillStr,"}",",");
        this.weaponsEquipId = weaponsEquipId;
        this.clotheEquipId = clotheEquipId;
        this.zhendaoId = zhendaoId;
        this.guanghuanId = guanghuanId;
        this.title = title;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("robotID:").append(robotID).append(";");
        str.append("level:").append(level).append(";");
        str.append("career:").append(career).append(";");
        str.append("name:").append(name).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("weaponsEquipId:").append(weaponsEquipId).append(";");
        str.append("clotheEquipId:").append(clotheEquipId).append(";");
        str.append("zhendaoId:").append(zhendaoId).append(";");
        str.append("guanghuanId:").append(guanghuanId).append(";");
        str.append("title:").append(title).append(";");
        return str.toString();
    }
}
