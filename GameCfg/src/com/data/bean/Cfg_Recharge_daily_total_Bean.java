/**
 * Auto generated, do not edit it
 *
 * recharge_daily_total配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Recharge_daily_total_Bean{
    /**
     * ID
     */
    private final int ID;
    /**
     * ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 类型（1每日，2累积）
     */
    private final int position;
    /**
     * 类型（1每日，2累积）
     * @return
     */
    public final int getPosition(){
        return position;
    }
    /**
     * position为1是挡位
position为2是累计天数
     */
    private final int day;
    /**
     * position为1是挡位
position为2是累计天数
     * @return
     */
    public final int getDay(){
        return day;
    }
    /**
     * 金额
     */
    private final int money;
    /**
     * 金额
     * @return
     */
    public final int getMoney(){
        return money;
    }
    /**
     * 奖励物品(物品ID_数量_绑定)(@;@_@)
     */
    private final ReadIntegerArrayEs award;
    /**
     * 奖励物品(物品ID_数量_绑定)(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAward(){
        return award;
    }
    /**
     * 距离开服N天开启
     */
    private final int startTime;
    /**
     * 距离开服N天开启
     * @return
     */
    public final int getStartTime(){
        return startTime;
    }
    /**
     * 距离开服N天结束
     */
    private final int endTime;
    /**
     * 距离开服N天结束
     * @return
     */
    public final int getEndTime(){
        return endTime;
    }

    public Cfg_Recharge_daily_total_Bean(int ID,int position,int day,int money,String awardStr,int startTime,int endTime){
        this.ID = ID;
        this.position = position;
        this.day = day;
        this.money = money;
        this.award = new ReadIntegerArrayEs(awardStr,"}",",");
        this.startTime = startTime;
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("position:").append(position).append(";");
        str.append("day:").append(day).append(";");
        str.append("money:").append(money).append(";");
        str.append("award:").append(award).append(";");
        str.append("startTime:").append(startTime).append(";");
        str.append("endTime:").append(endTime).append(";");
        return str.toString();
    }
}
