/**
 * Auto generated, do not edit it
 *
 * Equip_xianjia_synthesis配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Equip_xianjia_synthesis_Bean{
    /**
     * 装备ID
     */
    private final int Id;
    /**
     * 装备ID
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 合成出的装备ID
     */
    private final int Equip_ID;
    /**
     * 合成出的装备ID
     * @return
     */
    public final int getEquip_ID(){
        return Equip_ID;
    }
    /**
     * 合成所需的材料和消耗数量
     */
    private final ReadIntegerArray join_item;
    /**
     * 合成所需的材料和消耗数量
     * @return
     */
    public final ReadIntegerArray getJoin_item(){
        return join_item;
    }
    /**
     * 需要的等级（如果没到，则和不能合成显示为相同）
     */
    private final int limit_level;
    /**
     * 需要的等级（如果没到，则和不能合成显示为相同）
     * @return
     */
    public final int getLimit_level(){
        return limit_level;
    }

    public Cfg_Equip_xianjia_synthesis_Bean(int Id,int Equip_ID,String join_itemStr,int limit_level){
        this.Id = Id;
        this.Equip_ID = Equip_ID;
        this.join_item = new ReadIntegerArray(join_itemStr,",");
        this.limit_level = limit_level;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("Equip_ID:").append(Equip_ID).append(";");
        str.append("join_item:").append(join_item).append(";");
        str.append("limit_level:").append(limit_level).append(";");
        return str.toString();
    }
}
