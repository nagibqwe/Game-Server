/**
 * Auto generated, do not edit it
 *
 * HuaxingWing配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_HuaxingWing_Bean{
    /**
     * 模型ID
     */
    private final int id;
    /**
     * 模型ID
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
     * 排序
     */
    private final int order;
    /**
     * 排序
     * @return
     */
    public final int getOrder(){
        return order;
    }
    /**
     * 是否是时装（0，是；1不是：如果是时装，则不显示在化形界面中，只利用化形的出战等基础逻辑，不在化形中增加属性和技能）
     */
    private final int if_fashion;
    /**
     * 是否是时装（0，是；1不是：如果是时装，则不显示在化形界面中，只利用化形的出战等基础逻辑，不在化形中增加属性和技能）
     * @return
     */
    public final int getIf_fashion(){
        return if_fashion;
    }
    /**
     * 激活需要的条件（0物品激活，1服务器激活）
     */
    private final int active_condition;
    /**
     * 激活需要的条件（0物品激活，1服务器激活）
     * @return
     */
    public final int getActive_condition(){
        return active_condition;
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
    private final int Passive_skill;
    /**
     * 激活学习的技能
     * @return
     */
    public final int getPassive_skill(){
        return Passive_skill;
    }

    public Cfg_HuaxingWing_Bean(int id,String name,int order,int if_fashion,int active_condition,int active_item,String rent_attStr,String star_itemnumStr,int Passive_skill){
        this.id = id;
        this.name = name;
        this.order = order;
        this.if_fashion = if_fashion;
        this.active_condition = active_condition;
        this.active_item = active_item;
        this.rent_att = new ReadIntegerArrayEs(rent_attStr,"}",",");
        this.star_itemnum = new ReadIntegerArrayEs(star_itemnumStr,"}",",");
        this.Passive_skill = Passive_skill;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("order:").append(order).append(";");
        str.append("if_fashion:").append(if_fashion).append(";");
        str.append("active_condition:").append(active_condition).append(";");
        str.append("active_item:").append(active_item).append(";");
        str.append("rent_att:").append(rent_att).append(";");
        str.append("star_itemnum:").append(star_itemnum).append(";");
        str.append("Passive_skill:").append(Passive_skill).append(";");
        return str.toString();
    }
}
