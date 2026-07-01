/**
 * Auto generated, do not edit it
 *
 * Equip_Magic_resolve配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_Magic_resolve_Bean{
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
     * 拆解出来的物品和装备
     */
    private final ReadIntegerArrayEs resolveItem;
    /**
     * 拆解出来的物品和装备
     * @return
     */
    public final ReadIntegerArrayEs getResolveItem(){
        return resolveItem;
    }

    public Cfg_Equip_Magic_resolve_Bean(int Id,String resolveItemStr){
        this.Id = Id;
        this.resolveItem = new ReadIntegerArrayEs(resolveItemStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("resolveItem:").append(resolveItem).append(";");
        return str.toString();
    }
}
