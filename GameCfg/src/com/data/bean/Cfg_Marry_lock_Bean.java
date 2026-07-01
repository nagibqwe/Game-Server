/**
 * Auto generated, do not edit it
 *
 * marry_lock配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Marry_lock_Bean{
    /**
     * 心锁阶级
     */
    private final int level;
    /**
     * 心锁阶级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 心锁阶数
     */
    private final int stage;
    /**
     * 心锁阶数
     * @return
     */
    public final int getStage(){
        return stage;
    }
    /**
     * 心锁等级
     */
    private final int grade;
    /**
     * 心锁等级
     * @return
     */
    public final int getGrade(){
        return grade;
    }
    /**
     * 名称
     */
    private final String name;
    /**
     * 名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 基础属性
属性类型_属性(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 基础属性
属性类型_属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 仙侣属性
属性类型_属性(@;@_@)
     */
    private final ReadIntegerArrayEs marryAttribute;
    /**
     * 仙侣属性
属性类型_属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getMarryAttribute(){
        return marryAttribute;
    }
    /**
     * 升级需要经验
（当前级升级到下一级的经验）
     */
    private final int exp;
    /**
     * 升级需要经验
（当前级升级到下一级的经验）
     * @return
     */
    public final int getExp(){
        return exp;
    }
    /**
     * 升级消耗材料ID
     */
    private final ReadIntegerArray costItem;
    /**
     * 升级消耗材料ID
     * @return
     */
    public final ReadIntegerArray getCostItem(){
        return costItem;
    }
    /**
     * 单个材料经验
     */
    private final ReadIntegerArray singlexp;
    /**
     * 单个材料经验
     * @return
     */
    public final ReadIntegerArray getSinglexp(){
        return singlexp;
    }

    public Cfg_Marry_lock_Bean(int level,int stage,int grade,String name,String attributeStr,String marryAttributeStr,int exp,String costItemStr,String singlexpStr){
        this.level = level;
        this.stage = stage;
        this.grade = grade;
        this.name = name;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.marryAttribute = new ReadIntegerArrayEs(marryAttributeStr,"}",",");
        this.exp = exp;
        this.costItem = new ReadIntegerArray(costItemStr,",");
        this.singlexp = new ReadIntegerArray(singlexpStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("level:").append(level).append(";");
        str.append("stage:").append(stage).append(";");
        str.append("grade:").append(grade).append(";");
        str.append("name:").append(name).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("marryAttribute:").append(marryAttribute).append(";");
        str.append("exp:").append(exp).append(";");
        str.append("costItem:").append(costItem).append(";");
        str.append("singlexp:").append(singlexp).append(";");
        return str.toString();
    }
}
