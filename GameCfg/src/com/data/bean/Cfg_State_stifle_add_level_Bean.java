/**
 * Auto generated, do not edit it
 *
 * state_stifle_add_level配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_State_stifle_add_level_Bean{
    /**
     * ID（类型*100+进化等级）
     */
    private final int id;
    /**
     * ID（类型*100+进化等级）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 类型（1：经验器灵；2：战斗器灵；3：追击器灵）
     */
    private final int type;
    /**
     * 类型（1：经验器灵；2：战斗器灵；3：追击器灵）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 晋升等级
     */
    private final int level;
    /**
     * 晋升等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 晋升条件_数量
     */
    private final ReadIntegerArray need_item;
    /**
     * 晋升条件_数量
     * @return
     */
    public final ReadIntegerArray getNeed_item(){
        return need_item;
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
    /**
     * 是否为当前版本满级
     */
    private final int if_max;
    /**
     * 是否为当前版本满级
     * @return
     */
    public final int getIf_max(){
        return if_max;
    }
    /**
     * 需要的法宝等级ID
     */
    private final int need_level;
    /**
     * 需要的法宝等级ID
     * @return
     */
    public final int getNeed_level(){
        return need_level;
    }

    public Cfg_State_stifle_add_level_Bean(int id,int type,int level,String need_itemStr,String attributeStr,String per_attributeStr,String skillStr,String max_times,int if_max,int need_level){
        this.id = id;
        this.type = type;
        this.level = level;
        this.need_item = new ReadIntegerArray(need_itemStr,",");
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.per_attribute = new ReadIntegerArrayEs(per_attributeStr,"}",",");
        this.skill = new ReadIntegerArray(skillStr,",");
        this.max_times = max_times;
        this.if_max = if_max;
        this.need_level = need_level;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("level:").append(level).append(";");
        str.append("need_item:").append(need_item).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("per_attribute:").append(per_attribute).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("max_times:").append(max_times).append(";");
        str.append("if_max:").append(if_max).append(";");
        str.append("need_level:").append(need_level).append(";");
        return str.toString();
    }
}
