/**
 * Auto generated, do not edit it
 *
 * HuaxingFlySword配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_HuaxingFlySword_Bean{
    /**
     * 模型ID对应modelconfig表主键
     */
    private final int id;
    /**
     * 模型ID对应modelconfig表主键
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 翅膀名称
     */
    private final String name;
    /**
     * 翅膀名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 分类
     */
    private final int type;
    /**
     * 分类
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 激活需要的条件（0物品激活，1服务器激活，2，条件激活）
     */
    private final int active_condition;
    /**
     * 激活需要的条件（0物品激活，1服务器激活，2，条件激活）
     * @return
     */
    public final int getActive_condition(){
        return active_condition;
    }
    /**
     *  条件激活需要的条件
     */
    private final ReadIntegerArray Variable;
    /**
     *  条件激活需要的条件
     * @return
     */
    public final ReadIntegerArray getVariable(){
        return Variable;
    }
    /**
     * 激活需要的物品
     */
    private final int active_item;
    /**
     * 激活需要的物品
     * @return
     */
    public final int getActive_item(){
        return active_item;
    }
    /**
     * 属性类型_激活属性_升星属性(@;@_@)
     */
    private final ReadIntegerArrayEs rent_att;
    /**
     * 属性类型_激活属性_升星属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getRent_att(){
        return rent_att;
    }
    /**
     * 升星需要的数量 阶数_数量(@;@_@)
     */
    private final ReadIntegerArrayEs star_itemnum;
    /**
     * 升星需要的数量 阶数_数量(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getStar_itemnum(){
        return star_itemnum;
    }
    /**
     * 激活学习的技能
     */
    private final int passive_skill;
    /**
     * 激活学习的技能
     * @return
     */
    public final int getPassive_skill(){
        return passive_skill;
    }
    /**
     * 主动技能
     */
    private final int use_Skill;
    /**
     * 主动技能
     * @return
     */
    public final int getUse_Skill(){
        return use_Skill;
    }
    /**
     * 放技能的主角动作的技能
     */
    private final int player_Skill;
    /**
     * 放技能的主角动作的技能
     * @return
     */
    public final int getPlayer_Skill(){
        return player_Skill;
    }
    /**
     * 在剑灵主界面的板子资源(正面_背面）
     */
    private final ReadIntegerArray if_show;
    /**
     * 在剑灵主界面的板子资源(正面_背面）
     * @return
     */
    public final ReadIntegerArray getIf_show(){
        return if_show;
    }
    /**
     * 剑灵普通技能
     */
    private final int normal_Skill;
    /**
     * 剑灵普通技能
     * @return
     */
    public final int getNormal_Skill(){
        return normal_Skill;
    }
    /**
     * 背景描述
     */
    private final String describe;
    /**
     * 背景描述
     * @return
     */
    public final String getDescribe(){
        return describe;
    }

    public Cfg_HuaxingFlySword_Bean(int id,String name,int type,int active_condition,String VariableStr,int active_item,String rent_attStr,String star_itemnumStr,int passive_skill,int use_Skill,int player_Skill,String if_showStr,int normal_Skill,String describe){
        this.id = id;
        this.name = name;
        this.type = type;
        this.active_condition = active_condition;
        this.Variable = new ReadIntegerArray(VariableStr,",");
        this.active_item = active_item;
        this.rent_att = new ReadIntegerArrayEs(rent_attStr,"}",",");
        this.star_itemnum = new ReadIntegerArrayEs(star_itemnumStr,"}",",");
        this.passive_skill = passive_skill;
        this.use_Skill = use_Skill;
        this.player_Skill = player_Skill;
        this.if_show = new ReadIntegerArray(if_showStr,",");
        this.normal_Skill = normal_Skill;
        this.describe = describe;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("active_condition:").append(active_condition).append(";");
        str.append("Variable:").append(Variable).append(";");
        str.append("active_item:").append(active_item).append(";");
        str.append("rent_att:").append(rent_att).append(";");
        str.append("star_itemnum:").append(star_itemnum).append(";");
        str.append("passive_skill:").append(passive_skill).append(";");
        str.append("use_Skill:").append(use_Skill).append(";");
        str.append("player_Skill:").append(player_Skill).append(";");
        str.append("if_show:").append(if_show).append(";");
        str.append("normal_Skill:").append(normal_Skill).append(";");
        str.append("describe:").append(describe).append(";");
        return str.toString();
    }
}
