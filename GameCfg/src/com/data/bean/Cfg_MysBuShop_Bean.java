/**
 * Auto generated, do not edit it
 *
 * MysBuShop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_MysBuShop_Bean{
    /**
     * 商品列表唯一ID
     */
    private final int id;
    /**
     * 商品列表唯一ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 商品物品ID_数量【最多1个物品多个数量】(@;@_@)
     */
    private final ReadIntegerArrayEs item;
    /**
     * 商品物品ID_数量【最多1个物品多个数量】(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getItem(){
        return item;
    }
    /**
     * 兑换物ID_数量；ID_数量【最多5个】(@;@_@)
     */
    private final ReadIntegerArrayEs needitem;
    /**
     * 兑换物ID_数量；ID_数量【最多5个】(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getNeeditem(){
        return needitem;
    }
    /**
     * 每日限购（-1为不限购）
     */
    private final int day_buy;
    /**
     * 每日限购（-1为不限购）
     * @return
     */
    public final int getDay_buy(){
        return day_buy;
    }
    /**
     * 每周限购（-1为不限购）
     */
    private final int week_buy;
    /**
     * 每周限购（-1为不限购）
     * @return
     */
    public final int getWeek_buy(){
        return week_buy;
    }
    /**
     * 购买时是否有公告(1有公告，0无公告）
     */
    private final int notice;
    /**
     * 购买时是否有公告(1有公告，0无公告）
     * @return
     */
    public final int getNotice(){
        return notice;
    }

    public Cfg_MysBuShop_Bean(int id,String itemStr,String needitemStr,int day_buy,int week_buy,int notice){
        this.id = id;
        this.item = new ReadIntegerArrayEs(itemStr,"}",",");
        this.needitem = new ReadIntegerArrayEs(needitemStr,"}",",");
        this.day_buy = day_buy;
        this.week_buy = week_buy;
        this.notice = notice;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("item:").append(item).append(";");
        str.append("needitem:").append(needitem).append(";");
        str.append("day_buy:").append(day_buy).append(";");
        str.append("week_buy:").append(week_buy).append(";");
        str.append("notice:").append(notice).append(";");
        return str.toString();
    }
}
