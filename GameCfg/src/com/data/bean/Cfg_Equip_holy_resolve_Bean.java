/**
 * Auto generated, do not edit it
 *
 * Equip_holy_resolve配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_holy_resolve_Bean{
    /**
     * 装备id,(id构成：部位，职业，品质，等级）
     */
    private final int Id;
    /**
     * 装备id,(id构成：部位，职业，品质，等级）
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 1.分解
2.拆解
     */
    private final int type;
    /**
     * 1.分解
2.拆解
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 分解or拆解的货币_数量
type=1时只会获得item奖励
     */
    private final ReadIntegerArray item;
    /**
     * 分解or拆解的货币_数量
type=1时只会获得item奖励
     * @return
     */
    public final ReadIntegerArray getItem(){
        return item;
    }
    /**
     * 拆解出来的物品
type=2时，会同时获得item和holyEquip的奖励
     */
    private final ReadIntegerArrayEs holyEquip;
    /**
     * 拆解出来的物品
type=2时，会同时获得item和holyEquip的奖励
     * @return
     */
    public final ReadIntegerArrayEs getHolyEquip(){
        return holyEquip;
    }

    public Cfg_Equip_holy_resolve_Bean(int Id,int type,String itemStr,String holyEquipStr){
        this.Id = Id;
        this.type = type;
        this.item = new ReadIntegerArray(itemStr,",");
        this.holyEquip = new ReadIntegerArrayEs(holyEquipStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("type:").append(type).append(";");
        str.append("item:").append(item).append(";");
        str.append("holyEquip:").append(holyEquip).append(";");
        return str.toString();
    }
}
