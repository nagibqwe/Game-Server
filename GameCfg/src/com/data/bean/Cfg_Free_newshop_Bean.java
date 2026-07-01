/**
 * Auto generated, do not edit it
 *
 * free_newshop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Free_newshop_Bean{
    /**
     * 编号
     */
    private final int id;
    /**
     * 编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * ItemId_num_bind_occ
bind：0未绑定，1绑定
occ：0男1女9通用
     */
    private final ReadIntegerArrayEs item;
    /**
     * ItemId_num_bind_occ
bind：0未绑定，1绑定
occ：0男1女9通用
     * @return
     */
    public final ReadIntegerArrayEs getItem(){
        return item;
    }
    /**
     * 1:V4零元购
0:常规零元购
     */
    private final int type;
    /**
     * 1:V4零元购
0:常规零元购
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 价格
货币ID_货币数量
     */
    private final ReadIntegerArray price;
    /**
     * 价格
货币ID_货币数量
     * @return
     */
    public final ReadIntegerArray getPrice(){
        return price;
    }
    /**
     * 出现天数
     */
    private final int openTime;
    /**
     * 出现天数
     * @return
     */
    public final int getOpenTime(){
        return openTime;
    }
    /**
     * 达到指定开服天数返还消耗（超过天数购买立返）
     */
    private final int time;
    /**
     * 达到指定开服天数返还消耗（超过天数购买立返）
     * @return
     */
    public final int getTime(){
        return time;
    }

    public Cfg_Free_newshop_Bean(int id,String itemStr,int type,String priceStr,int openTime,int time){
        this.id = id;
        this.item = new ReadIntegerArrayEs(itemStr,"}",",");
        this.type = type;
        this.price = new ReadIntegerArray(priceStr,",");
        this.openTime = openTime;
        this.time = time;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("item:").append(item).append(";");
        str.append("type:").append(type).append(";");
        str.append("price:").append(price).append(";");
        str.append("openTime:").append(openTime).append(";");
        str.append("time:").append(time).append(";");
        return str.toString();
    }
}
