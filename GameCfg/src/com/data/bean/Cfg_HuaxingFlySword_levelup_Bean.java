/**
 * Auto generated, do not edit it
 *
 * HuaxingFlySword_levelup配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_HuaxingFlySword_levelup_Bean{
    /**
     * 剑灵等级
     */
    private final int id;
    /**
     * 剑灵等级
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 本级属性(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 本级属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 升阶物品id_数量
     */
    private final ReadIntegerArrayEs up_item;
    /**
     * 升阶物品id_数量
     * @return
     */
    public final ReadIntegerArrayEs getUp_item(){
        return up_item;
    }

    public Cfg_HuaxingFlySword_levelup_Bean(int id,String attributeStr,String up_itemStr){
        this.id = id;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.up_item = new ReadIntegerArrayEs(up_itemStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("up_item:").append(up_item).append(";");
        return str.toString();
    }
}
