/**
 * Auto generated, do not edit it
 *
 * immortal_soul_synthesis配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Immortal_soul_synthesis_Bean{
    /**
     * 目标id
     */
    private final int id;
    /**
     * 目标id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 目标仙魄名称
     */
    private final String target_items;
    /**
     * 目标仙魄名称
     * @return
     */
    public final String getTarget_items(){
        return target_items;
    }
    /**
     * 所属分类id
     */
    private final int type;
    /**
     * 所属分类id
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 分类名称
     */
    private final String type_name;
    /**
     * 分类名称
     * @return
     */
    public final String getType_name(){
        return type_name;
    }
    /**
     * 所需仙魄1
     */
    private final ReadIntegerArrayEs material1;
    /**
     * 所需仙魄1
     * @return
     */
    public final ReadIntegerArrayEs getMaterial1(){
        return material1;
    }
    /**
     * 所需道具
     */
    private final ReadIntegerArrayEs material2;
    /**
     * 所需道具
     * @return
     */
    public final ReadIntegerArrayEs getMaterial2(){
        return material2;
    }
    /**
     * 成功概率(万分位)
     */
    private final int probability;
    /**
     * 成功概率(万分位)
     * @return
     */
    public final int getProbability(){
        return probability;
    }

    public Cfg_Immortal_soul_synthesis_Bean(int id,String target_items,int type,String type_name,String material1Str,String material2Str,int probability){
        this.id = id;
        this.target_items = target_items;
        this.type = type;
        this.type_name = type_name;
        this.material1 = new ReadIntegerArrayEs(material1Str,"}",",");
        this.material2 = new ReadIntegerArrayEs(material2Str,"}",",");
        this.probability = probability;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("target_items:").append(target_items).append(";");
        str.append("type:").append(type).append(";");
        str.append("type_name:").append(type_name).append(";");
        str.append("material1:").append(material1).append(";");
        str.append("material2:").append(material2).append(";");
        str.append("probability:").append(probability).append(";");
        return str.toString();
    }
}
