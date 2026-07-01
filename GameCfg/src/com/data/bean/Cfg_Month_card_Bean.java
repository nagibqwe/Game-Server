/**
 * Auto generated, do not edit it
 *
 * month_card配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Month_card_Bean{
    /**
     * 月卡尊享卡表
（程序写死处理的ID，不能轻易改变，如果改变需要通知客户端
     */
    private final int id;
    /**
     * 月卡尊享卡表
（程序写死处理的ID，不能轻易改变，如果改变需要通知客户端
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 可领天数，-1为永久
     */
    private final int day;
    /**
     * 可领天数，-1为永久
     * @return
     */
    public final int getDay(){
        return day;
    }
    /**
     * 激活花费货币类型_货币数量
0代表内购，走rechargeitem
非0代表货币，走item表
     */
    private final ReadIntegerArray cost;
    /**
     * 激活花费货币类型_货币数量
0代表内购，走rechargeitem
非0代表货币，走item表
     * @return
     */
    public final ReadIntegerArray getCost(){
        return cost;
    }
    /**
     * 首次激活获得的id_num_是否绑定（0否 1是）(@;@_@)
     */
    private final ReadIntegerArrayEs nowitem;
    /**
     * 首次激活获得的id_num_是否绑定（0否 1是）(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getNowitem(){
        return nowitem;
    }
    /**
     * 每天可领的id_num_是否绑定（0否 1是）(@;@_@)
     */
    private final ReadIntegerArrayEs dayAddItem;
    /**
     * 每天可领的id_num_是否绑定（0否 1是）(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getDayAddItem(){
        return dayAddItem;
    }
    /**
     * 经验额外百分比加成（额外考虑是否保留）
     */
    private final int expAddPer;
    /**
     * 经验额外百分比加成（额外考虑是否保留）
     * @return
     */
    public final int getExpAddPer(){
        return expAddPer;
    }
    /**
     * 特权卡对应聚宝盆激活的加成（万分比）
废弃
     */
    private final int magicBowl;
    /**
     * 特权卡对应聚宝盆激活的加成（万分比）
废弃
     * @return
     */
    public final int getMagicBowl(){
        return magicBowl;
    }

    public Cfg_Month_card_Bean(int id,int day,String costStr,String nowitemStr,String dayAddItemStr,int expAddPer,int magicBowl){
        this.id = id;
        this.day = day;
        this.cost = new ReadIntegerArray(costStr,",");
        this.nowitem = new ReadIntegerArrayEs(nowitemStr,"}",",");
        this.dayAddItem = new ReadIntegerArrayEs(dayAddItemStr,"}",",");
        this.expAddPer = expAddPer;
        this.magicBowl = magicBowl;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("day:").append(day).append(";");
        str.append("cost:").append(cost).append(";");
        str.append("nowitem:").append(nowitem).append(";");
        str.append("dayAddItem:").append(dayAddItem).append(";");
        str.append("expAddPer:").append(expAddPer).append(";");
        str.append("magicBowl:").append(magicBowl).append(";");
        return str.toString();
    }
}
