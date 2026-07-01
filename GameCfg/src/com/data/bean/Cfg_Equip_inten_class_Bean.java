/**
 * Auto generated, do not edit it
 *
 * equip_inten_class配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_inten_class_Bean{
    /**
     * 流水号
     */
    private final int Id;
    /**
     * 流水号
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 强化等级要求
     */
    private final int Level;
    /**
     * 强化等级要求
     * @return
     */
    public final int getLevel(){
        return Level;
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
    /**
     * 枚举id大于1000的局部属性(@;@_@)
     */
    private final ReadIntegerArrayEs Value1;
    /**
     * 枚举id大于1000的局部属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getValue1(){
        return Value1;
    }

    public Cfg_Equip_inten_class_Bean(int Id,int Level,String ValueStr,String Value1Str){
        this.Id = Id;
        this.Level = Level;
        this.Value = new ReadIntegerArrayEs(ValueStr,"}",",");
        this.Value1 = new ReadIntegerArrayEs(Value1Str,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("Level:").append(Level).append(";");
        str.append("Value:").append(Value).append(";");
        str.append("Value1:").append(Value1).append(";");
        return str.toString();
    }
}
