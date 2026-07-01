/**
 * Auto generated, do not edit it
 *
 * pet_level配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Pet_level_Bean{
    /**
     * 等级
     */
    private final int level;
    /**
     * 等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 经验
     */
    private final int exp;
    /**
     * 经验
     * @return
     */
    public final int getExp(){
        return exp;
    }
    /**
     * 属性：属性类型_数值，属性类型1_数值，(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 属性：属性类型_数值，属性类型1_数值，(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 战斗力
     */
    private final int power;
    /**
     * 战斗力
     * @return
     */
    public final int getPower(){
        return power;
    }

    public Cfg_Pet_level_Bean(int level,int exp,String attributeStr,int power){
        this.level = level;
        this.exp = exp;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.power = power;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("level:").append(level).append(";");
        str.append("exp:").append(exp).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("power:").append(power).append(";");
        return str.toString();
    }
}
