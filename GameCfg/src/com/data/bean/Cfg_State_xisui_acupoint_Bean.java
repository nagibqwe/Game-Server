/**
 * Auto generated, do not edit it
 *
 * state_xisui_acupoint配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_State_xisui_acupoint_Bean{
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
     * 星窍名
     */
    private final String name;
    /**
     * 星窍名
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 洗髓玩法（1.玄体 2.灵体 3.金身 4.玉体 5.仙体）
     */
    private final int group;
    /**
     * 洗髓玩法（1.玄体 2.灵体 3.金身 4.玉体 5.仙体）
     * @return
     */
    public final int getGroup(){
        return group;
    }
    /**
     * 属性（覆盖）：属性类型_数值(@;@_@)
     */
    private final ReadIntegerArrayEs prop_all;
    /**
     * 属性（覆盖）：属性类型_数值(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getProp_all(){
        return prop_all;
    }
    /**
     * 属性（累加）：属性类型_数值(@;@_@)
     */
    private final ReadIntegerArrayEs prop_star_all;
    /**
     * 属性（累加）：属性类型_数值(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getProp_star_all(){
        return prop_star_all;
    }
    /**
     * 属性（累加）：属性类型_数值(@;@_@)
     */
    private final ReadIntegerArrayEs prop_add;
    /**
     * 属性（累加）：属性类型_数值(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getProp_add(){
        return prop_add;
    }
    /**
     * 消耗道具ID_数量
     */
    private final ReadIntegerArray item_cost;
    /**
     * 消耗道具ID_数量
     * @return
     */
    public final ReadIntegerArray getItem_cost(){
        return item_cost;
    }
    /**
     * 消耗货币ID_数量
     */
    private final ReadIntegerArray coin_cost;
    /**
     * 消耗货币ID_数量
     * @return
     */
    public final ReadIntegerArray getCoin_cost(){
        return coin_cost;
    }

    public Cfg_State_xisui_acupoint_Bean(int id,String name,int group,String prop_allStr,String prop_star_allStr,String prop_addStr,String item_costStr,String coin_costStr){
        this.id = id;
        this.name = name;
        this.group = group;
        this.prop_all = new ReadIntegerArrayEs(prop_allStr,"}",",");
        this.prop_star_all = new ReadIntegerArrayEs(prop_star_allStr,"}",",");
        this.prop_add = new ReadIntegerArrayEs(prop_addStr,"}",",");
        this.item_cost = new ReadIntegerArray(item_costStr,",");
        this.coin_cost = new ReadIntegerArray(coin_costStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("group:").append(group).append(";");
        str.append("prop_all:").append(prop_all).append(";");
        str.append("prop_star_all:").append(prop_star_all).append(";");
        str.append("prop_add:").append(prop_add).append(";");
        str.append("item_cost:").append(item_cost).append(";");
        str.append("coin_cost:").append(coin_cost).append(";");
        return str.toString();
    }
}
