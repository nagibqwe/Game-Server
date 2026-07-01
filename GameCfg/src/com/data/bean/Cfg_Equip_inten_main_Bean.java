/**
 * Auto generated, do not edit it
 *
 * equip_inten_main配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Equip_inten_main_Bean{
    /**
     * 流水号
     */
    private final int ID;
    /**
     * 流水号
     * @return
     */
    public final int getID(){
        return ID;
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
     * 强化消耗
     */
    private final ReadIntegerArrayEs consume;
    /**
     * 强化消耗
     * @return
     */
    public final ReadIntegerArrayEs getConsume(){
        return consume;
    }
    /**
     * 获取熟练度(下限,上限)(@_@) client ignore
     */
    private final ReadIntegerArray Proficiency;
    /**
     * 获取熟练度(下限,上限)(@_@) client ignore
     * @return
     */
    public final ReadIntegerArray getProficiency(){
        return Proficiency;
    }
    /**
     * 熟练度上限
     */
    private final int Proficiency_Max;
    /**
     * 熟练度上限
     * @return
     */
    public final int getProficiency_Max(){
        return Proficiency_Max;
    }
    /**
     * 属性(@;@_@)
     */
    private final ReadIntegerArrayEs Value;
    /**
     * 属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getValue(){
        return Value;
    }

    public Cfg_Equip_inten_main_Bean(int ID,int Type,int Level,String consumeStr,String ProficiencyStr,int Proficiency_Max,String ValueStr){
        this.ID = ID;
        this.Type = Type;
        this.Level = Level;
        this.consume = new ReadIntegerArrayEs(consumeStr,"}",",");
        this.Proficiency = new ReadIntegerArray(ProficiencyStr,",");
        this.Proficiency_Max = Proficiency_Max;
        this.Value = new ReadIntegerArrayEs(ValueStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("Type:").append(Type).append(";");
        str.append("Level:").append(Level).append(";");
        str.append("consume:").append(consume).append(";");
        str.append("Proficiency:").append(Proficiency).append(";");
        str.append("Proficiency_Max:").append(Proficiency_Max).append(";");
        str.append("Value:").append(Value).append(";");
        return str.toString();
    }
}
