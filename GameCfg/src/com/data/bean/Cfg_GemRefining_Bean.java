/**
 * Auto generated, do not edit it
 *
 * GemRefining配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_GemRefining_Bean{
    /**
     * 流水id(部位*1000+等级)
     */
    private final int Id;
    /**
     * 流水id(部位*1000+等级)
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 宝石精炼部位id
     */
    private final int part_id;
    /**
     * 宝石精炼部位id
     * @return
     */
    public final int getPart_id(){
        return part_id;
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
     * 普通宝石精炼属性(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 普通宝石精炼属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 普通宝石精炼百分比属性(@;@_@)
     */
    private final ReadIntegerArrayEs attribute_proportion;
    /**
     * 普通宝石精炼百分比属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_proportion(){
        return attribute_proportion;
    }
    /**
     * 普通宝石升当前级经验
     */
    private final int exp;
    /**
     * 普通宝石升当前级经验
     * @return
     */
    public final int getExp(){
        return exp;
    }
    /**
     * 普通宝石消耗材料: 材料1_材料2_材料3(@_@)
     */
    private final ReadIntegerArray itemID;
    /**
     * 普通宝石消耗材料: 材料1_材料2_材料3(@_@)
     * @return
     */
    public final ReadIntegerArray getItemID(){
        return itemID;
    }

    public Cfg_GemRefining_Bean(int Id,int part_id,int level,String attributeStr,String attribute_proportionStr,int exp,String itemIDStr){
        this.Id = Id;
        this.part_id = part_id;
        this.level = level;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.attribute_proportion = new ReadIntegerArrayEs(attribute_proportionStr,"}",",");
        this.exp = exp;
        this.itemID = new ReadIntegerArray(itemIDStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("part_id:").append(part_id).append(";");
        str.append("level:").append(level).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("attribute_proportion:").append(attribute_proportion).append(";");
        str.append("exp:").append(exp).append(";");
        str.append("itemID:").append(itemID).append(";");
        return str.toString();
    }
}
