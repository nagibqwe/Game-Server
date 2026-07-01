/**
 * Auto generated, do not edit it
 *
 * state_stifle_add_level_all配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_State_stifle_add_level_all_Bean{
    /**
     * ID
     */
    private final int id;
    /**
     * ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 晋升总等级
     */
    private final int level_all;
    /**
     * 晋升总等级
     * @return
     */
    public final int getLevel_all(){
        return level_all;
    }
    /**
     * 核心核心属性
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 核心核心属性
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 核心百分比属性
     */
    private final ReadIntegerArrayEs per_attribute;
    /**
     * 核心百分比属性
     * @return
     */
    public final ReadIntegerArrayEs getPer_attribute(){
        return per_attribute;
    }
    /**
     * 核心技能
     */
    private final ReadIntegerArray skill;
    /**
     * 核心技能
     * @return
     */
    public final ReadIntegerArray getSkill(){
        return skill;
    }
    /**
     * 界面上核心属性描述
     */
    private final String max_times;
    /**
     * 界面上核心属性描述
     * @return
     */
    public final String getMax_times(){
        return max_times;
    }

    public Cfg_State_stifle_add_level_all_Bean(int id,int level_all,String attributeStr,String per_attributeStr,String skillStr,String max_times){
        this.id = id;
        this.level_all = level_all;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.per_attribute = new ReadIntegerArrayEs(per_attributeStr,"}",",");
        this.skill = new ReadIntegerArray(skillStr,",");
        this.max_times = max_times;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("level_all:").append(level_all).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("per_attribute:").append(per_attribute).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("max_times:").append(max_times).append(";");
        return str.toString();
    }
}
