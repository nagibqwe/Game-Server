/**
 * Auto generated, do not edit it
 *
 * jjcrobot配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Jjcrobot_Bean{
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
     * 名次最小多少名,用于确定机器人属性
     */
    private final int rank_mix;
    /**
     * 名次最小多少名,用于确定机器人属性
     * @return
     */
    public final int getRank_mix(){
        return rank_mix;
    }
    /**
     * 名次最大名
     */
    private final int rank_max;
    /**
     * 名次最大名
     * @return
     */
    public final int getRank_max(){
        return rank_max;
    }
    /**
     * 阵营
     */
    private final int camp;
    /**
     * 阵营
     * @return
     */
    public final int getCamp(){
        return camp;
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
     * 武器ID
     */
    private final int weaponsEquipId;
    /**
     * 武器ID
     * @return
     */
    public final int getWeaponsEquipId(){
        return weaponsEquipId;
    }
    /**
     * 武器星级
     */
    private final int weaponStar;
    /**
     * 武器星级
     * @return
     */
    public final int getWeaponStar(){
        return weaponStar;
    }
    /**
     * 衣服ID
     */
    private final int clotheEquipId;
    /**
     * 衣服ID
     * @return
     */
    public final int getClotheEquipId(){
        return clotheEquipId;
    }
    /**
     * 衣服星级
     */
    private final int clothesStar;
    /**
     * 衣服星级
     * @return
     */
    public final int getClothesStar(){
        return clothesStar;
    }
    /**
     * 时装ID
     */
    private final int fashionId;
    /**
     * 时装ID
     * @return
     */
    public final int getFashionId(){
        return fashionId;
    }
    /**
     * 翅膀ID
     */
    private final int wingId;
    /**
     * 翅膀ID
     * @return
     */
    public final int getWingId(){
        return wingId;
    }

    public Cfg_Jjcrobot_Bean(int robotID,String name,int rank_mix,int rank_max,int camp,int career,String attributeStr,String skillStr,int weaponsEquipId,int weaponStar,int clotheEquipId,int clothesStar,int fashionId,int wingId){
        this.robotID = robotID;
        this.name = name;
        this.rank_mix = rank_mix;
        this.rank_max = rank_max;
        this.camp = camp;
        this.career = career;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.skill = new ReadIntegerArrayEs(skillStr,"}",",");
        this.weaponsEquipId = weaponsEquipId;
        this.weaponStar = weaponStar;
        this.clotheEquipId = clotheEquipId;
        this.clothesStar = clothesStar;
        this.fashionId = fashionId;
        this.wingId = wingId;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("robotID:").append(robotID).append(";");
        str.append("name:").append(name).append(";");
        str.append("rank_mix:").append(rank_mix).append(";");
        str.append("rank_max:").append(rank_max).append(";");
        str.append("camp:").append(camp).append(";");
        str.append("career:").append(career).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("weaponsEquipId:").append(weaponsEquipId).append(";");
        str.append("weaponStar:").append(weaponStar).append(";");
        str.append("clotheEquipId:").append(clotheEquipId).append(";");
        str.append("clothesStar:").append(clothesStar).append(";");
        str.append("fashionId:").append(fashionId).append(";");
        str.append("wingId:").append(wingId).append(";");
        return str.toString();
    }
}
