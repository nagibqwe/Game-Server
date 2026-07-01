/**
 * Auto generated, do not edit it
 *
 * GemstoneInlay配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_GemstoneInlay_Bean{
    /**
     * 流水id(宝石=1000+部位；仙玉=2000+部位)
     */
    private final int Id;
    /**
     * 流水id(宝石=1000+部位；仙玉=2000+部位)
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 宝石消耗颜色类型区分,同一类型的消耗材料要一致,用于智能精炼
     */
    private final int color_type;
    /**
     * 宝石消耗颜色类型区分,同一类型的消耗材料要一致,用于智能精炼
     * @return
     */
    public final int getColor_type(){
        return color_type;
    }
    /**
     * 可镶嵌宝石id(按宝石等级 从小到大 配置)...宝石1id_宝石2id…(@_@)
     */
    private final ReadIntegerArray Gemstone_id;
    /**
     * 可镶嵌宝石id(按宝石等级 从小到大 配置)...宝石1id_宝石2id…(@_@)
     * @return
     */
    public final ReadIntegerArray getGemstone_id(){
        return Gemstone_id;
    }
    /**
     * 开孔上限
     */
    private final int Limit_Number;
    /**
     * 开孔上限
     * @return
     */
    public final int getLimit_Number(){
        return Limit_Number;
    }
    /**
     * 开孔条件.条件都需要定义在functionVariable中.1孔条件_数据;2孔的条件_数据;…….(@;@_@)
     */
    private final ReadIntegerArrayEs Location_condition;
    /**
     * 开孔条件.条件都需要定义在functionVariable中.1孔条件_数据;2孔的条件_数据;…….(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getLocation_condition(){
        return Location_condition;
    }

    public Cfg_GemstoneInlay_Bean(int Id,int color_type,String Gemstone_idStr,int Limit_Number,String Location_conditionStr){
        this.Id = Id;
        this.color_type = color_type;
        this.Gemstone_id = new ReadIntegerArray(Gemstone_idStr,",");
        this.Limit_Number = Limit_Number;
        this.Location_condition = new ReadIntegerArrayEs(Location_conditionStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("color_type:").append(color_type).append(";");
        str.append("Gemstone_id:").append(Gemstone_id).append(";");
        str.append("Limit_Number:").append(Limit_Number).append(";");
        str.append("Location_condition:").append(Location_condition).append(";");
        return str.toString();
    }
}
