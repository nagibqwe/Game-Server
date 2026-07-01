/**
 * Auto generated, do not edit it
 *
 * Horse_equip_soulbound_class配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Horse_equip_soulbound_class_Bean{
    /**
     * 流水号
脉轮*10000+顺序号
     */
    private final int Id;
    /**
     * 流水号
脉轮*10000+顺序号
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 脉轮id
     */
    private final int site;
    /**
     * 脉轮id
     * @return
     */
    public final int getSite(){
        return site;
    }
    /**
     * 升灵套装目标等级
     */
    private final int SuitLevel;
    /**
     * 升灵套装目标等级
     * @return
     */
    public final int getSuitLevel(){
        return SuitLevel;
    }
    /**
     * 套装属性
     */
    private final ReadIntegerArrayEs Value;
    /**
     * 套装属性
     * @return
     */
    public final ReadIntegerArrayEs getValue(){
        return Value;
    }

    public Cfg_Horse_equip_soulbound_class_Bean(int Id,int site,int SuitLevel,String ValueStr){
        this.Id = Id;
        this.site = site;
        this.SuitLevel = SuitLevel;
        this.Value = new ReadIntegerArrayEs(ValueStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("site:").append(site).append(";");
        str.append("SuitLevel:").append(SuitLevel).append(";");
        str.append("Value:").append(Value).append(";");
        return str.toString();
    }
}
