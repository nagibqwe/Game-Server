package com.gm.project.stat.stat_daily_data.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;


/**
 *
 * 
 * @author gm
 * @date 2021-08-06
 */
public class StatDailyDataBean extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 等级 */
    @Excel(name = "日期")
    private String day;
    /** dau */
    @Excel(name = "充值金额")
    private Float totalmoney ;
    @Excel(name = "充值元宝")
    private Float totalgold;

    @Excel(name = "充值次数")
    private String totaltimes;

    @Excel(name = "付费人数")
    private String totaluser;

    @Excel(name = "活跃付费率")
    private Float activpayrate;

    @Excel(name = "活跃玩家")
    private String activenum;

    @Excel(name = "新增玩家")
    private String addnum;


    @Excel(name = "新增付费玩家人数")
    private String addRechargeNum;


    @Excel(name = "新增付费率")
    private Float addpayrate;


    @Excel(name = "活跃设备")
    private String deviceNum;

    @Excel(name = "新增设备")
    private String deviceaddnum;


    @Excel(name = "arpu")
    private Float arpu;

    @Excel(name = "arppu")
    private Float arppu;

    @Excel(name = "平均在线")
    private Float avgnum;
    @Excel(name = "最高在线")
    private Integer maxnum;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }



    public String getDeviceaddnum() {
        return deviceaddnum;
    }

    public void setDeviceaddnum(String deviceaddnum) {
        this.deviceaddnum = deviceaddnum;
    }






    public String getActivenum() {
        return activenum;
    }

    public void setActivenum(String activenum) {
        this.activenum = activenum;
    }

    public String getAddnum() {
        return addnum;
    }

    public void setAddnum(String addnum) {
        this.addnum = addnum;
    }

    public String getAddRechargeNum() {
        return addRechargeNum;
    }

    public void setAddRechargeNum(String addRechargeNum) {
        this.addRechargeNum = addRechargeNum;
    }

    public Float getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(Float totalmoney) {
        this.totalmoney = totalmoney;
    }

    public Float getTotalgold() {
        return totalgold;
    }

    public void setTotalgold(Float totalgold) {
        this.totalgold = totalgold;
    }



    public String getTotaluser() {
        return totaluser;
    }

    public void setTotaluser(String totaluser) {
        this.totaluser = totaluser;
    }

    public String getTotaltimes() {
        return totaltimes;
    }

    public void setTotaltimes(String totaltimes) {
        this.totaltimes = totaltimes;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }


    public Float getAddpayrate() {
        return addpayrate;
    }

    public void setAddpayrate(Float addpayrate) {
        this.addpayrate = addpayrate;
    }

    public Float getActivpayrate() {
        return activpayrate;
    }

    public void setActivpayrate(Float activpayrate) {
        this.activpayrate = activpayrate;
    }

    public Float getArpu() {
        return arpu;
    }

    public void setArpu(Float arpu) {
        this.arpu = arpu;
    }

    public Float getArppu() {
        return arppu;
    }

    public void setArppu(Float arppu) {
        this.arppu = arppu;
    }

    public Float getAvgnum() {
        return avgnum;
    }

    public void setAvgnum(Float avgnum) {
        this.avgnum = avgnum;
    }

    public Integer getMaxnum() {
        return maxnum;
    }

    public void setMaxnum(Integer maxnum) {
        this.maxnum = maxnum;
    }
}
