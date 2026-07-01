/**
 * Auto generated, do not edit it
 *
 * HappyWeek配置表
 */
package com.data.bean;

	
public class Cfg_HappyWeek_Bean{
    /**
     * ID（天数*100+顺序）
     */
    private final int id;
    /**
     * ID（天数*100+顺序）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 第几天的狂欢
     */
    private final int day;
    /**
     * 第几天的狂欢
     * @return
     */
    public final int getDay(){
        return day;
    }
    /**
     * 顺序
     */
    private final int ord;
    /**
     * 顺序
     * @return
     */
    public final int getOrd(){
        return ord;
    }
    /**
     * 购买描述
     */
    private final String des;
    /**
     * 购买描述
     * @return
     */
    public final String getDes(){
        return des;
    }
    /**
     * 内购ID
     */
    private final int rechargeID;
    /**
     * 内购ID
     * @return
     */
    public final int getRechargeID(){
        return rechargeID;
    }

    public Cfg_HappyWeek_Bean(int id,int day,int ord,String des,int rechargeID){
        this.id = id;
        this.day = day;
        this.ord = ord;
        this.des = des;
        this.rechargeID = rechargeID;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("day:").append(day).append(";");
        str.append("ord:").append(ord).append(";");
        str.append("des:").append(des).append(";");
        str.append("rechargeID:").append(rechargeID).append(";");
        return str.toString();
    }
}
