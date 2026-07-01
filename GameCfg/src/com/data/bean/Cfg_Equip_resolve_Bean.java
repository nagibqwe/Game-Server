/**
 * Auto generated, do not edit it
 *
 * Equip_resolve配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_resolve_Bean{
    /**
     * 装备id
     */
    private final int Id;
    /**
     * 装备id
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 拆解的装备_数量

     */
    private final ReadIntegerArray Equip;
    /**
     * 拆解的装备_数量

     * @return
     */
    public final ReadIntegerArray getEquip(){
        return Equip;
    }
    /**
     * 拆解的物品_数量；拆解的物品_数量；
     */
    private final ReadIntegerArrayEs item;
    /**
     * 拆解的物品_数量；拆解的物品_数量；
     * @return
     */
    public final ReadIntegerArrayEs getItem(){
        return item;
    }

    public Cfg_Equip_resolve_Bean(int Id,String EquipStr,String itemStr){
        this.Id = Id;
        this.Equip = new ReadIntegerArray(EquipStr,",");
        this.item = new ReadIntegerArrayEs(itemStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("Equip:").append(Equip).append(";");
        str.append("item:").append(item).append(";");
        return str.toString();
    }
}
