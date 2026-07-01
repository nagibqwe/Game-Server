/**
 * Auto generated, do not edit it
 *
 * Cross_devil_card_Camp配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Cross_devil_card_Camp_Bean{
    /**
     * 阵营编号
     */
    private final int id;
    /**
     * 阵营编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 阵营名字
     */
    private final String name;
    /**
     * 阵营名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 阵营对应的装备部位
对应Equip表的part字段
     */
    private final ReadIntegerArrayEs parts_list;
    /**
     * 阵营对应的装备部位
对应Equip表的part字段
     * @return
     */
    public final ReadIntegerArrayEs getParts_list(){
        return parts_list;
    }
    /**
     * 开启条件
对应FunctionVariable
填空代表默认开启
     */
    private final ReadIntegerArrayEs condition;
    /**
     * 开启条件
对应FunctionVariable
填空代表默认开启
     * @return
     */
    public final ReadIntegerArrayEs getCondition(){
        return condition;
    }

    public Cfg_Cross_devil_card_Camp_Bean(int id,String name,String parts_listStr,String conditionStr){
        this.id = id;
        this.name = name;
        this.parts_list = new ReadIntegerArrayEs(parts_listStr,"}",",");
        this.condition = new ReadIntegerArrayEs(conditionStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("parts_list:").append(parts_list).append(";");
        str.append("condition:").append(condition).append(";");
        return str.toString();
    }
}
