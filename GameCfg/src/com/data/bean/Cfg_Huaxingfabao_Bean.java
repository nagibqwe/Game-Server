/**
 * Auto generated, do not edit it
 *
 * Huaxingfabao配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Huaxingfabao_Bean{
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
     * 类型（1，等级给的；2，化形给的）
     */
    private final int type;
    /**
     * 类型（1，等级给的；2，化形给的）
     * @return
     */
    public final int getType(){
        return type;
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
     * 法宝伤害成长（激活数值_升星数值）
     */
    private final ReadIntegerArray fabaohit;
    /**
     * 法宝伤害成长（激活数值_升星数值）
     * @return
     */
    public final ReadIntegerArray getFabaohit(){
        return fabaohit;
    }
    /**
     * 升星需要的数量  阶数_数量(@;@_@)
     */
    private final ReadIntegerArrayEs star_itemnum;
    /**
     * 升星需要的数量  阶数_数量(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getStar_itemnum(){
        return star_itemnum;
    }
    /**
     * 法宝使用的技能
     */
    private final int use_skill;
    /**
     * 法宝使用的技能
     * @return
     */
    public final int getUse_skill(){
        return use_skill;
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
    /**
     * 0表示不显示装备，1显示人物装备2显示宠物装备，3显示坐骑装备
     */
    private final int isShow;
    /**
     * 0表示不显示装备，1显示人物装备2显示宠物装备，3显示坐骑装备
     * @return
     */
    public final int getIsShow(){
        return isShow;
    }

    public Cfg_Huaxingfabao_Bean(int id,String name,int if_fashion,int type,int active_item,String rent_attStr,String fabaohitStr,String star_itemnumStr,int use_skill,int Passive_skill,int isShow){
        this.id = id;
        this.name = name;
        this.if_fashion = if_fashion;
        this.type = type;
        this.active_item = active_item;
        this.rent_att = new ReadIntegerArrayEs(rent_attStr,"}",",");
        this.fabaohit = new ReadIntegerArray(fabaohitStr,",");
        this.star_itemnum = new ReadIntegerArrayEs(star_itemnumStr,"}",",");
        this.use_skill = use_skill;
        this.Passive_skill = Passive_skill;
        this.isShow = isShow;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("if_fashion:").append(if_fashion).append(";");
        str.append("type:").append(type).append(";");
        str.append("active_item:").append(active_item).append(";");
        str.append("rent_att:").append(rent_att).append(";");
        str.append("fabaohit:").append(fabaohit).append(";");
        str.append("star_itemnum:").append(star_itemnum).append(";");
        str.append("use_skill:").append(use_skill).append(";");
        str.append("Passive_skill:").append(Passive_skill).append(";");
        str.append("isShow:").append(isShow).append(";");
        return str.toString();
    }
}
