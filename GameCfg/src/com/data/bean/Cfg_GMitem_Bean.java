/**
 * Auto generated, do not edit it
 *
 * GMitem配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_GMitem_Bean{
    /**
     * 命令ID（加在&addbag命令后
     */
    private final int Id;
    /**
     * 命令ID（加在&addbag命令后
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 奖励物品（物品ID_数量）
     */
    private final ReadIntegerArrayEs item;
    /**
     * 奖励物品（物品ID_数量）
     * @return
     */
    public final ReadIntegerArrayEs getItem(){
        return item;
    }

    public Cfg_GMitem_Bean(int Id,String itemStr){
        this.Id = Id;
        this.item = new ReadIntegerArrayEs(itemStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("item:").append(item).append(";");
        return str.toString();
    }
}
