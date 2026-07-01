/**
 * Auto generated, do not edit it
 *
 * item_special_gift配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Item_special_gift_Bean{
    /**
     * 物品ID
     */
    private final int id;
    /**
     * 物品ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 打开后可选择的物品_数量
     */
    private final ReadIntegerArrayEs item;
    /**
     * 打开后可选择的物品_数量
     * @return
     */
    public final ReadIntegerArrayEs getItem(){
        return item;
    }

    public Cfg_Item_special_gift_Bean(int id,String itemStr){
        this.id = id;
        this.item = new ReadIntegerArrayEs(itemStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("item:").append(item).append(";");
        return str.toString();
    }
}
