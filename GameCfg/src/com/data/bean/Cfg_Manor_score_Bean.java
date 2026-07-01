/**
 * Auto generated, do not edit it
 *
 * manor_score配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Manor_score_Bean{
    /**
     * key=积分， 表示积分数量
     */
    private final int id;
    /**
     * key=积分， 表示积分数量
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 展示的道具，实际给与的奖励
     */
    private final int item;
    /**
     * 展示的道具，实际给与的奖励
     * @return
     */
    public final int getItem(){
        return item;
    }
    /**
     * 展示的道具
     */
    private final ReadIntegerArray show_item;
    /**
     * 展示的道具
     * @return
     */
    public final ReadIntegerArray getShow_item(){
        return show_item;
    }

    public Cfg_Manor_score_Bean(int id,int item,String show_itemStr){
        this.id = id;
        this.item = item;
        this.show_item = new ReadIntegerArray(show_itemStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("item:").append(item).append(";");
        str.append("show_item:").append(show_item).append(";");
        return str.toString();
    }
}
