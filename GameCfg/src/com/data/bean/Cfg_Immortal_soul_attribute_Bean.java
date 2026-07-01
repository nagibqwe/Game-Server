/**
 * Auto generated, do not edit it
 *
 * immortal_soul_attribute配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Immortal_soul_attribute_Bean{
    /**
     * 仙魄id(6000000+type*100000+quality*1000+exclusive_ID)
     */
    private final int id;
    /**
     * 仙魄id(6000000+type*100000+quality*1000+exclusive_ID)
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
     * 仙魄类型,1属性仙魄.2经验类仙魄3扩展仙魄
     */
    private final int type;
    /**
     * 仙魄类型,1属性仙魄.2经验类仙魄3扩展仙魄
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 品质(3蓝,4紫,6金,7红)
     */
    private final int quality;
    /**
     * 品质(3蓝,4紫,6金,7红)
     * @return
     */
    public final int getQuality(){
        return quality;
    }
    /**
     * 展示的星星数
     */
    private final int star;
    /**
     * 展示的星星数
     * @return
     */
    public final int getStar(){
        return star;
    }
    /**
     * 镶嵌互斥类型
比对互斥id,相同id互斥
     */
    private final int exclusive_ID;
    /**
     * 镶嵌互斥类型
比对互斥id,相同id互斥
     * @return
     */
    public final int getExclusive_ID(){
        return exclusive_ID;
    }
    /**
     * icon
     */
    private final int icon;
    /**
     * icon
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 基础属性
     */
    private final ReadIntegerArrayEs demand_value;
    /**
     * 基础属性
     * @return
     */
    public final ReadIntegerArrayEs getDemand_value(){
        return demand_value;
    }
    /**
     * 每级加成属性
     */
    private final ReadIntegerArrayEs basic_attributes;
    /**
     * 每级加成属性
     * @return
     */
    public final ReadIntegerArrayEs getBasic_attributes(){
        return basic_attributes;
    }
    /**
     * 基础百分比属性
     */
    private final ReadIntegerArrayEs demand_value_percent;
    /**
     * 基础百分比属性
     * @return
     */
    public final ReadIntegerArrayEs getDemand_value_percent(){
        return demand_value_percent;
    }
    /**
     * 每级百分比加成属性
     */
    private final ReadIntegerArrayEs basic_attributes_percent;
    /**
     * 每级百分比加成属性
     * @return
     */
    public final ReadIntegerArrayEs getBasic_attributes_percent(){
        return basic_attributes_percent;
    }
    /**
     * 兑换所需仙魄积分
     */
    private final ReadIntegerArray exchange_Consumption;
    /**
     * 兑换所需仙魄积分
     * @return
     */
    public final ReadIntegerArray getExchange_Consumption(){
        return exchange_Consumption;
    }
    /**
     * 兑换排序,从小到大排序,从左到右的排列((0或空则不再兑换界面显示)
     */
    private final int exchange_ranking;
    /**
     * 兑换排序,从小到大排序,从左到右的排列((0或空则不再兑换界面显示)
     * @return
     */
    public final int getExchange_ranking(){
        return exchange_ranking;
    }
    /**
     * 初始分解经验
     */
    private final int exp;
    /**
     * 初始分解经验
     * @return
     */
    public final int getExp(){
        return exp;
    }
    /**
     * 总览显示条件,层数_行数_列数
     */
    private final ReadIntegerArray overview_conditions;
    /**
     * 总览显示条件,层数_行数_列数
     * @return
     */
    public final ReadIntegerArray getOverview_conditions(){
        return overview_conditions;
    }
    /**
     * 所有获取所需爬塔层数(FunctionVariablea中条件)
     */
    private final ReadIntegerArray exchange_conditions;
    /**
     * 所有获取所需爬塔层数(FunctionVariablea中条件)
     * @return
     */
    public final ReadIntegerArray getExchange_conditions(){
        return exchange_conditions;
    }
    /**
     * 仙魄归类,用来判断镶嵌和替换时是否为同类
     */
    private final int type2;
    /**
     * 仙魄归类,用来判断镶嵌和替换时是否为同类
     * @return
     */
    public final int getType2(){
        return type2;
    }
    /**
     * 等级上限
     */
    private final int level_max;
    /**
     * 等级上限
     * @return
     */
    public final int getLevel_max(){
        return level_max;
    }
    /**
     * 可镶嵌的格子ID
     */
    private final ReadIntegerArray grid;
    /**
     * 可镶嵌的格子ID
     * @return
     */
    public final ReadIntegerArray getGrid(){
        return grid;
    }

    public Cfg_Immortal_soul_attribute_Bean(int id,String name,int type,int quality,int star,int exclusive_ID,int icon,String demand_valueStr,String basic_attributesStr,String demand_value_percentStr,String basic_attributes_percentStr,String exchange_ConsumptionStr,int exchange_ranking,int exp,String overview_conditionsStr,String exchange_conditionsStr,int type2,int level_max,String gridStr){
        this.id = id;
        this.name = name;
        this.type = type;
        this.quality = quality;
        this.star = star;
        this.exclusive_ID = exclusive_ID;
        this.icon = icon;
        this.demand_value = new ReadIntegerArrayEs(demand_valueStr,"}",",");
        this.basic_attributes = new ReadIntegerArrayEs(basic_attributesStr,"}",",");
        this.demand_value_percent = new ReadIntegerArrayEs(demand_value_percentStr,"}",",");
        this.basic_attributes_percent = new ReadIntegerArrayEs(basic_attributes_percentStr,"}",",");
        this.exchange_Consumption = new ReadIntegerArray(exchange_ConsumptionStr,",");
        this.exchange_ranking = exchange_ranking;
        this.exp = exp;
        this.overview_conditions = new ReadIntegerArray(overview_conditionsStr,",");
        this.exchange_conditions = new ReadIntegerArray(exchange_conditionsStr,",");
        this.type2 = type2;
        this.level_max = level_max;
        this.grid = new ReadIntegerArray(gridStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("quality:").append(quality).append(";");
        str.append("star:").append(star).append(";");
        str.append("exclusive_ID:").append(exclusive_ID).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("demand_value:").append(demand_value).append(";");
        str.append("basic_attributes:").append(basic_attributes).append(";");
        str.append("demand_value_percent:").append(demand_value_percent).append(";");
        str.append("basic_attributes_percent:").append(basic_attributes_percent).append(";");
        str.append("exchange_Consumption:").append(exchange_Consumption).append(";");
        str.append("exchange_ranking:").append(exchange_ranking).append(";");
        str.append("exp:").append(exp).append(";");
        str.append("overview_conditions:").append(overview_conditions).append(";");
        str.append("exchange_conditions:").append(exchange_conditions).append(";");
        str.append("type2:").append(type2).append(";");
        str.append("level_max:").append(level_max).append(";");
        str.append("grid:").append(grid).append(";");
        return str.toString();
    }
}
