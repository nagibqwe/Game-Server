/**
 * Auto generated, do not edit it
 *
 * Equip_Collection_start配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_Collection_start_Bean{
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
     * 大阶段
     */
    private final int grade;
    /**
     * 大阶段
     * @return
     */
    public final int getGrade(){
        return grade;
    }
    /**
     * 需要的人物等级
     */
    private final int level;
    /**
     * 需要的人物等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 需要的物品ID_数量
     */
    private final ReadIntegerArray needitem;
    /**
     * 需要的物品ID_数量
     * @return
     */
    public final ReadIntegerArray getNeeditem(){
        return needitem;
    }
    /**
     * 本级属性(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 本级属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 大阶段的名字
     */
    private final String name;
    /**
     * 大阶段的名字
     * @return
     */
    public final String getName(){
        return name;
    }

    public Cfg_Equip_Collection_start_Bean(int Id,int grade,int level,String needitemStr,String attributeStr,String name){
        this.Id = Id;
        this.grade = grade;
        this.level = level;
        this.needitem = new ReadIntegerArray(needitemStr,",");
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.name = name;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("grade:").append(grade).append(";");
        str.append("level:").append(level).append(";");
        str.append("needitem:").append(needitem).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("name:").append(name).append(";");
        return str.toString();
    }
}
