/**
 * Auto generated, do not edit it
 *
 * Equip_holy_synthesis配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Equip_holy_synthesis_Bean{
    /**
     * 消耗的装备ID
     */
    private final int Id;
    /**
     * 消耗的装备ID
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
     * 合成额外消耗道具_数量
     */
    private final ReadIntegerArray join_item;
    /**
     * 合成额外消耗道具_数量
     * @return
     */
    public final ReadIntegerArray getJoin_item(){
        return join_item;
    }

    public Cfg_Equip_holy_synthesis_Bean(int Id,int Equip_ID,String join_itemStr){
        this.Id = Id;
        this.Equip_ID = Equip_ID;
        this.join_item = new ReadIntegerArray(join_itemStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("Equip_ID:").append(Equip_ID).append(";");
        str.append("join_item:").append(join_item).append(";");
        return str.toString();
    }
}
