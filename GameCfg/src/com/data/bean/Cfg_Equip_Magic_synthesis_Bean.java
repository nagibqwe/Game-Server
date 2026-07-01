/**
 * Auto generated, do not edit it
 *
 * Equip_Magic_synthesis配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Equip_Magic_synthesis_Bean{
    /**
     * 合成出的装备ID
     */
    private final int Id;
    /**
     * 合成出的装备ID
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 合成消耗的装备
     */
    private final ReadIntegerArray needEquip;
    /**
     * 合成消耗的装备
     * @return
     */
    public final ReadIntegerArray getNeedEquip(){
        return needEquip;
    }
    /**
     * 合成消耗的物品
     */
    private final ReadIntegerArray needItem;
    /**
     * 合成消耗的物品
     * @return
     */
    public final ReadIntegerArray getNeedItem(){
        return needItem;
    }

    public Cfg_Equip_Magic_synthesis_Bean(int Id,String needEquipStr,String needItemStr){
        this.Id = Id;
        this.needEquip = new ReadIntegerArray(needEquipStr,",");
        this.needItem = new ReadIntegerArray(needItemStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("needEquip:").append(needEquip).append(";");
        str.append("needItem:").append(needItem).append(";");
        return str.toString();
    }
}
