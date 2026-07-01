/**
 * Auto generated, do not edit it
 *
 * fashion配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Fashion_Bean{
    /**
     * 时装ID(type*100000000+（0，时装；1，化形养成）*10000000+化形ID(衣服武器用优先级排序）
     */
    private final int id;
    /**
     * 时装ID(type*100000000+（0，时装；1，化形养成）*10000000+化形ID(衣服武器用优先级排序）
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
     * 类型（1，衣服；2武器；3，背饰；4坐骑；5，宠物；6法宝；11头像；12头像框；13气泡）
     */
    private final int type;
    /**
     * 类型（1，衣服；2武器；3，背饰；4坐骑；5，宠物；6法宝；11头像；12头像框；13气泡）
     * @return
     */
    public final int getType(){
        return type;
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
     * 资源ID
     */
    private final ReadIntegerArrayEs res;
    /**
     * 资源ID
     * @return
     */
    public final ReadIntegerArrayEs getRes(){
        return res;
    }
    /**
     * 激活需要的物品(男_女）
     */
    private final ReadIntegerArrayEs active_item;
    /**
     * 激活需要的物品(男_女）
     * @return
     */
    public final ReadIntegerArrayEs getActive_item(){
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

    public Cfg_Fashion_Bean(int id,String name,int type,int order,String resStr,String active_itemStr,String rent_attStr,String star_itemnumStr,int Passive_skill){
        this.id = id;
        this.name = name;
        this.type = type;
        this.order = order;
        this.res = new ReadIntegerArrayEs(resStr,"}",",");
        this.active_item = new ReadIntegerArrayEs(active_itemStr,"}",",");
        this.rent_att = new ReadIntegerArrayEs(rent_attStr,"}",",");
        this.star_itemnum = new ReadIntegerArrayEs(star_itemnumStr,"}",",");
        this.Passive_skill = Passive_skill;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("order:").append(order).append(";");
        str.append("res:").append(res).append(";");
        str.append("active_item:").append(active_item).append(";");
        str.append("rent_att:").append(rent_att).append(";");
        str.append("star_itemnum:").append(star_itemnum).append(";");
        str.append("Passive_skill:").append(Passive_skill).append(";");
        return str.toString();
    }
}
