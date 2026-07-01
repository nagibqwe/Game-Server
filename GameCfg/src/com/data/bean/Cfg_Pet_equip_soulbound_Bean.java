/**
 * Auto generated, do not edit it
 *
 * pet_equip_soulbound配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Pet_equip_soulbound_Bean{
    /**
     * 流水号
槽位*100000000+部位*10000 +附魂等级
     */
    private final int Id;
    /**
     * 流水号
槽位*100000000+部位*10000 +附魂等级
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 槽位id
     */
    private final int site;
    /**
     * 槽位id
     * @return
     */
    public final int getSite(){
        return site;
    }
    /**
     * 部位类型
     */
    private final int Type;
    /**
     * 部位类型
     * @return
     */
    public final int getType(){
        return Type;
    }
    /**
     * 附魂阶级
     */
    private final int Level;
    /**
     * 附魂阶级
     * @return
     */
    public final int getLevel(){
        return Level;
    }
    /**
     * 附魂消耗（道具_数量）
     */
    private final ReadIntegerArrayEs Consume;
    /**
     * 附魂消耗（道具_数量）
     * @return
     */
    public final ReadIntegerArrayEs getConsume(){
        return Consume;
    }
    /**
     * 装备基础属性加成
（不含装备强化附加的属性，万分比格式 ）
     */
    private final int PercentValue;
    /**
     * 装备基础属性加成
（不含装备强化附加的属性，万分比格式 ）
     * @return
     */
    public final int getPercentValue(){
        return PercentValue;
    }
    /**
     * 附魂增加的属性
     */
    private final ReadIntegerArrayEs ExtraValue;
    /**
     * 附魂增加的属性
     * @return
     */
    public final ReadIntegerArrayEs getExtraValue(){
        return ExtraValue;
    }

    public Cfg_Pet_equip_soulbound_Bean(int Id,int site,int Type,int Level,String ConsumeStr,int PercentValue,String ExtraValueStr){
        this.Id = Id;
        this.site = site;
        this.Type = Type;
        this.Level = Level;
        this.Consume = new ReadIntegerArrayEs(ConsumeStr,"}",",");
        this.PercentValue = PercentValue;
        this.ExtraValue = new ReadIntegerArrayEs(ExtraValueStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("site:").append(site).append(";");
        str.append("Type:").append(Type).append(";");
        str.append("Level:").append(Level).append(";");
        str.append("Consume:").append(Consume).append(";");
        str.append("PercentValue:").append(PercentValue).append(";");
        str.append("ExtraValue:").append(ExtraValue).append(";");
        return str.toString();
    }
}
