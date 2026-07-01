/**
 * Auto generated, do not edit it
 *
 * free_shop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Free_shop_Bean{
    /**
     * 商城编号
编号ID必须和rechargeItem的ID保持一致
     */
    private final int id;
    /**
     * 商城编号
编号ID必须和rechargeItem的ID保持一致
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
     * 价格
type_value
type:-1=内购，type=其他值的时候对应item表的货币ID
     */
    private final ReadIntegerArray price;
    /**
     * 价格
type_value
type:-1=内购，type=其他值的时候对应item表的货币ID
     * @return
     */
    public final ReadIntegerArray getPrice(){
        return price;
    }
    /**
     * 返还价格的天数
     */
    private final int time;
    /**
     * 返还价格的天数
     * @return
     */
    public final int getTime(){
        return time;
    }
    /**
     * 每天返还可领取货币
     */
    private final ReadIntegerArray rewardDaily;
    /**
     * 每天返还可领取货币
     * @return
     */
    public final ReadIntegerArray getRewardDaily(){
        return rewardDaily;
    }

    public Cfg_Free_shop_Bean(int id,String itemStr,String priceStr,int time,String rewardDailyStr){
        this.id = id;
        this.item = new ReadIntegerArrayEs(itemStr,"}",",");
        this.price = new ReadIntegerArray(priceStr,",");
        this.time = time;
        this.rewardDaily = new ReadIntegerArray(rewardDailyStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("item:").append(item).append(";");
        str.append("price:").append(price).append(";");
        str.append("time:").append(time).append(";");
        str.append("rewardDaily:").append(rewardDaily).append(";");
        return str.toString();
    }
}
