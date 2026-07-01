/**
 * Auto generated, do not edit it
 *
 * pet_equip_inten配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Pet_equip_inten_Bean{
    /**
     * 流水号
槽位*100000000+部位*10000+流水号
     */
    private final int Id;
    /**
     * 流水号
槽位*100000000+部位*10000+流水号
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 槽位id
     */
    private final int site;
    /**
     * 槽位id
     * @return
     */
    public final int getSite(){
        return site;
    }
    /**
     * 部位类型
     */
    private final int Type;
    /**
     * 部位类型
     * @return
     */
    public final int getType(){
        return Type;
    }
    /**
     * 强化等级
     */
    private final int Level;
    /**
     * 强化等级
     * @return
     */
    public final int getLevel(){
        return Level;
    }
    /**
     * 强化消耗（道具_数量）
     */
    private final ReadIntegerArrayEs Consume;
    /**
     * 强化消耗（道具_数量）
     * @return
     */
    public final ReadIntegerArrayEs getConsume(){
        return Consume;
    }
    /**
     * 强化加成属性
（@_@)
     */
    private final ReadIntegerArrayEs Value;
    /**
     * 强化加成属性
（@_@)
     * @return
     */
    public final ReadIntegerArrayEs getValue(){
        return Value;
    }

    public Cfg_Pet_equip_inten_Bean(int Id,int site,int Type,int Level,String ConsumeStr,String ValueStr){
        this.Id = Id;
        this.site = site;
        this.Type = Type;
        this.Level = Level;
        this.Consume = new ReadIntegerArrayEs(ConsumeStr,"}",",");
        this.Value = new ReadIntegerArrayEs(ValueStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("site:").append(site).append(";");
        str.append("Type:").append(Type).append(";");
        str.append("Level:").append(Level).append(";");
        str.append("Consume:").append(Consume).append(";");
        str.append("Value:").append(Value).append(";");
        return str.toString();
    }
}
