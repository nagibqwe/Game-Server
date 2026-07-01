/**
 * Auto generated, do not edit it
 *
 * Equip_Collection配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_Collection_Bean{
    /**
     * ID（职业*100+阶数）
     */
    private final int Id;
    /**
     * ID（职业*100+阶数）
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 灵体的名字
     */
    private final String name;
    /**
     * 灵体的名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 灵体的阶数
     */
    private final int grade;
    /**
     * 灵体的阶数
     * @return
     */
    public final int getGrade(){
        return grade;
    }
    /**
     * 职业限制
0-男剑
1-女枪
2-待定
3-待定
4-待定
5-待定
9-通用
     */
    private final int gender;
    /**
     * 职业限制
0-男剑
1-女枪
2-待定
3-待定
4-待定
5-待定
9-通用
     * @return
     */
    public final int getGender(){
        return gender;
    }
    /**
     * 灵体开启需要的人物等级
     */
    private final int describe;
    /**
     * 灵体开启需要的人物等级
     * @return
     */
    public final int getDescribe(){
        return describe;
    }
    /**
     * 需要的最低战斗力装备（部位ID_装备ID）
     */
    private final ReadIntegerArray equip;
    /**
     * 需要的最低战斗力装备（部位ID_装备ID）
     * @return
     */
    public final ReadIntegerArray getEquip(){
        return equip;
    }
    /**
     * 激活基础灵力
     */
    private final ReadIntegerArrayEs att;
    /**
     * 激活基础灵力
     * @return
     */
    public final ReadIntegerArrayEs getAtt(){
        return att;
    }
    /**
     * 需要的红色5星装备件数
     */
    private final int standard_power;
    /**
     * 需要的红色5星装备件数
     * @return
     */
    public final int getStandard_power(){
        return standard_power;
    }
    /**
     * 战力达到后基础灵力
     */
    private final ReadIntegerArrayEs att1;
    /**
     * 战力达到后基础灵力
     * @return
     */
    public final ReadIntegerArrayEs getAtt1(){
        return att1;
    }
    /**
     * 特殊百分比称号
     */
    private final ReadIntegerArray att2;
    /**
     * 特殊百分比称号
     * @return
     */
    public final ReadIntegerArray getAtt2(){
        return att2;
    }
    /**
     * 激活的称号ID
     */
    private final int activit_title;
    /**
     * 激活的称号ID
     * @return
     */
    public final int getActivit_title(){
        return activit_title;
    }

    public Cfg_Equip_Collection_Bean(int Id,String name,int grade,int gender,int describe,String equipStr,String attStr,int standard_power,String att1Str,String att2Str,int activit_title){
        this.Id = Id;
        this.name = name;
        this.grade = grade;
        this.gender = gender;
        this.describe = describe;
        this.equip = new ReadIntegerArray(equipStr,",");
        this.att = new ReadIntegerArrayEs(attStr,"}",",");
        this.standard_power = standard_power;
        this.att1 = new ReadIntegerArrayEs(att1Str,"}",",");
        this.att2 = new ReadIntegerArray(att2Str,",");
        this.activit_title = activit_title;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("name:").append(name).append(";");
        str.append("grade:").append(grade).append(";");
        str.append("gender:").append(gender).append(";");
        str.append("describe:").append(describe).append(";");
        str.append("equip:").append(equip).append(";");
        str.append("att:").append(att).append(";");
        str.append("standard_power:").append(standard_power).append(";");
        str.append("att1:").append(att1).append(";");
        str.append("att2:").append(att2).append(";");
        str.append("activit_title:").append(activit_title).append(";");
        return str.toString();
    }
}
