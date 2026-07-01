/**
 * Auto generated, do not edit it
 *
 * Equip_xianjia_exchange配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Equip_xianjia_exchange_Bean{
    /**
     * ID
     */
    private final int Id;
    /**
     * ID
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 主要分类（0，仙甲；1，八卦）
     */
    private final int main_type;
    /**
     * 主要分类（0，仙甲；1，八卦）
     * @return
     */
    public final int getMain_type(){
        return main_type;
    }
    /**
     * 是第几套
     */
    private final int type;
    /**
     * 是第几套
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 装备ID
     */
    private final int equipid;
    /**
     * 装备ID
     * @return
     */
    public final int getEquipid(){
        return equipid;
    }
    /**
     * 职业（0男；1女）
     */
    private final int occ;
    /**
     * 职业（0男；1女）
     * @return
     */
    public final int getOcc(){
        return occ;
    }
    /**
     * 需要的货币及数量
     */
    private final ReadIntegerArray needitem;
    /**
     * 需要的货币及数量
     * @return
     */
    public final ReadIntegerArray getNeeditem(){
        return needitem;
    }

    public Cfg_Equip_xianjia_exchange_Bean(int Id,int main_type,int type,int equipid,int occ,String needitemStr){
        this.Id = Id;
        this.main_type = main_type;
        this.type = type;
        this.equipid = equipid;
        this.occ = occ;
        this.needitem = new ReadIntegerArray(needitemStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("main_type:").append(main_type).append(";");
        str.append("type:").append(type).append(";");
        str.append("equipid:").append(equipid).append(";");
        str.append("occ:").append(occ).append(";");
        str.append("needitem:").append(needitem).append(";");
        return str.toString();
    }
}
