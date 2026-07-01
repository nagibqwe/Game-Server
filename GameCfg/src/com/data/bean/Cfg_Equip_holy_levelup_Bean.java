/**
 * Auto generated, do not edit it
 *
 * Equip_holy_levelup配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_holy_levelup_Bean{
    /**
     * ID(type*10000+level)
     */
    private final int id;
    /**
     * ID(type*10000+level)
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 符文种类(对应装备表圣装的type)
(0履、1裤、2腕、3武、4衣、5冠、6项链、7戒指、8配饰、9耳环、10斗心)（client ignore）
     */
    private final int type;
    /**
     * 符文种类(对应装备表圣装的type)
(0履、1裤、2腕、3武、4衣、5冠、6项链、7戒指、8配饰、9耳环、10斗心)（client ignore）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 等级（client ignore）
     */
    private final int level;
    /**
     * 等级（client ignore）
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 强化本级属性(@;@_@)此处为装备本级属性，而不是成长属性，界面显示成长需要下一级减去当前级
     */
    private final ReadIntegerArrayEs att;
    /**
     * 强化本级属性(@;@_@)此处为装备本级属性，而不是成长属性，界面显示成长需要下一级减去当前级
     * @return
     */
    public final ReadIntegerArrayEs getAtt(){
        return att;
    }
    /**
     * 每次强化到下一级消耗圣装精粹
     */
    private final int cost;
    /**
     * 每次强化到下一级消耗圣装精粹
     * @return
     */
    public final int getCost(){
        return cost;
    }

    public Cfg_Equip_holy_levelup_Bean(int id,int type,int level,String attStr,int cost){
        this.id = id;
        this.type = type;
        this.level = level;
        this.att = new ReadIntegerArrayEs(attStr,"}",",");
        this.cost = cost;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("level:").append(level).append(";");
        str.append("att:").append(att).append(";");
        str.append("cost:").append(cost).append(";");
        return str.toString();
    }
}
