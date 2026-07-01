/**
 * Auto generated, do not edit it
 *
 * pet_soul配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Pet_soul_Bean{
    /**
     * id
     */
    private final int Id;
    /**
     * id
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 阶数，每多少级为1阶
     */
    private final int stage;
    /**
     * 阶数，每多少级为1阶
     * @return
     */
    public final int getStage(){
        return stage;
    }
    /**
     * 提升等级上限
     */
    private final int consumption_max;
    /**
     * 提升等级上限
     * @return
     */
    public final int getConsumption_max(){
        return consumption_max;
    }
    /**
     * 消耗资源：资源ID
     */
    private final int consume;
    /**
     * 消耗资源：资源ID
     * @return
     */
    public final int getConsume(){
        return consume;
    }
    /**
     * 属性：属性类型_数值，属性类型1_数值，(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 属性：属性类型_数值，属性类型1_数值，(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 用于客户端显示
     */
    private final String name;
    /**
     * 用于客户端显示
     * @return
     */
    public final String getName(){
        return name;
    }

    public Cfg_Pet_soul_Bean(int Id,int stage,int consumption_max,int consume,String attributeStr,String name){
        this.Id = Id;
        this.stage = stage;
        this.consumption_max = consumption_max;
        this.consume = consume;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.name = name;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("stage:").append(stage).append(";");
        str.append("consumption_max:").append(consumption_max).append(";");
        str.append("consume:").append(consume).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("name:").append(name).append(";");
        return str.toString();
    }
}
