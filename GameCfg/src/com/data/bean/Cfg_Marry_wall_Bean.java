/**
 * Auto generated, do not edit it
 *
 * marry_wall配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Marry_wall_Bean{
    /**
     * 宣言ID
     */
    private final int id;
    /**
     * 宣言ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 消耗的货币
     */
    private final ReadIntegerArray cost_item;
    /**
     * 消耗的货币
     * @return
     */
    public final ReadIntegerArray getCost_item(){
        return cost_item;
    }
    /**
     * 名称
     */
    private final String desc;
    /**
     * 名称
     * @return
     */
    public final String getDesc(){
        return desc;
    }

    public Cfg_Marry_wall_Bean(int id,String cost_itemStr,String desc){
        this.id = id;
        this.cost_item = new ReadIntegerArray(cost_itemStr,",");
        this.desc = desc;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("cost_item:").append(cost_item).append(";");
        str.append("desc:").append(desc).append(";");
        return str.toString();
    }
}
