/**
 * Auto generated, do not edit it
 *
 * skill_meridian_new配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Skill_meridian_new_Bean{
    /**
     * key值（职业*1000000+所属经脉*10000+格子*100+等级）
     */
    private final int id;
    /**
     * key值（职业*1000000+所属经脉*10000+格子*100+等级）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 
     */
    private final int icon;
    /**
     * 
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 所属职业
     */
    private final int occ;
    /**
     * 所属职业
     * @return
     */
    public final int getOcc(){
        return occ;
    }
    /**
     * 所属经脉大类
     */
    private final int type;
    /**
     * 所属经脉大类
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 经脉ID
     */
    private final int meridian_id;
    /**
     * 经脉ID
     * @return
     */
    public final int getMeridian_id(){
        return meridian_id;
    }
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
     * 最大等级，方便读取
     */
    private final int max_level;
    /**
     * 最大等级，方便读取
     * @return
     */
    public final int getMax_level(){
        return max_level;
    }
    /**
     * 经脉增加的属性
     */
    private final ReadIntegerArrayEs add_att;
    /**
     * 经脉增加的属性
     * @return
     */
    public final ReadIntegerArrayEs getAdd_att(){
        return add_att;
    }
    /**
     * 经脉增加的被动技能
     */
    private final int add_passive_skill;
    /**
     * 经脉增加的被动技能
     * @return
     */
    public final int getAdd_passive_skill(){
        return add_passive_skill;
    }
    /**
     * 经脉增加的主动技能
     */
    private final int add_skill;
    /**
     * 经脉增加的主动技能
     * @return
     */
    public final int getAdd_skill(){
        return add_skill;
    }
    /**
     * 需要父经脉id，使用本表的id字段
     */
    private final int need_parent_id;
    /**
     * 需要父经脉id，使用本表的id字段
     * @return
     */
    public final int getNeed_parent_id(){
        return need_parent_id;
    }
    /**
     * 学习需要货币_点数
     */
    private final ReadIntegerArray need_value;
    /**
     * 学习需要货币_点数
     * @return
     */
    public final ReadIntegerArray getNeed_value(){
        return need_value;
    }

    public Cfg_Skill_meridian_new_Bean(int id,int icon,int occ,int type,int meridian_id,int level,int max_level,String add_attStr,int add_passive_skill,int add_skill,int need_parent_id,String need_valueStr){
        this.id = id;
        this.icon = icon;
        this.occ = occ;
        this.type = type;
        this.meridian_id = meridian_id;
        this.level = level;
        this.max_level = max_level;
        this.add_att = new ReadIntegerArrayEs(add_attStr,"}",",");
        this.add_passive_skill = add_passive_skill;
        this.add_skill = add_skill;
        this.need_parent_id = need_parent_id;
        this.need_value = new ReadIntegerArray(need_valueStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("occ:").append(occ).append(";");
        str.append("type:").append(type).append(";");
        str.append("meridian_id:").append(meridian_id).append(";");
        str.append("level:").append(level).append(";");
        str.append("max_level:").append(max_level).append(";");
        str.append("add_att:").append(add_att).append(";");
        str.append("add_passive_skill:").append(add_passive_skill).append(";");
        str.append("add_skill:").append(add_skill).append(";");
        str.append("need_parent_id:").append(need_parent_id).append(";");
        str.append("need_value:").append(need_value).append(";");
        return str.toString();
    }
}
