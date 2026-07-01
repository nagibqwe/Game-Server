package com.gm.project.stat.stat_recharge_overview.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 *
 * 
 * @author gm
 * @date 2021-08-06
 */
public class StatRechargeOverviewBean extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    /**
     *日期
     */
    private String date;
    /**
     * 付费总额
     */
    private Double totalPayment;
    /**
     * DAU
     */
    private Long DAU;
    /**
     * 当天新增账号
     */
    private Long newUsers;
    /**
     * 当天付费账号
     */
    private Long rechargeUsers;
    /**
     * 新增付费账号
     */
    private Long newRechargeUsers;
    /**
     * 老玩家付费账号
     */
    private Long oldRechargeUsers;

    /**
     * 新增玩家付费率
     */
    private String newRechargeRate;
    /**
     * 老玩家付费率
     */
    private String oldRechargeRate;
    /**
     * 付费率
     */
    private String rechargeRate;
    /**
     * ARPU
     */
    private String ARPU;
    /**
     * ARPPU
     */
    private String ARPPU;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Long getDAU() {
        return DAU;
    }

    public void setDAU(Long DAU) {
        this.DAU = DAU;
    }

    public Long getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(Long newUsers) {
        this.newUsers = newUsers;
    }

    public Long getRechargeUsers() {
        return rechargeUsers;
    }

    public void setRechargeUsers(Long rechargeUsers) {
        this.rechargeUsers = rechargeUsers;
    }

    public Long getNewRechargeUsers() {
        return newRechargeUsers;
    }

    public void setNewRechargeUsers(Long newRechargeUsers) {
        this.newRechargeUsers = newRechargeUsers;
    }

    public Long getOldRechargeUsers() {
        return oldRechargeUsers;
    }

    public void setOldRechargeUsers(Long oldRechargeUsers) {
        this.oldRechargeUsers = oldRechargeUsers;
    }

    public String getRechargeRate() {
        return rechargeRate;
    }

    public void setRechargeRate(String rechargeRate) {
        this.rechargeRate = rechargeRate;
    }

    public String getARPU() {
        return ARPU;
    }

    public void setARPU(String ARPU) {
        this.ARPU = ARPU;
    }

    public String getARPPU() {
        return ARPPU;
    }

    public void setARPPU(String ARPPU) {
        this.ARPPU = ARPPU;
    }

    public String getNewRechargeRate() {
        return newRechargeRate;
    }

    public void setNewRechargeRate(String newRechargeRate) {
        this.newRechargeRate = newRechargeRate;
    }

    public String getOldRechargeRate() {
        return oldRechargeRate;
    }

    public void setOldRechargeRate(String oldRechargeRate) {
        this.oldRechargeRate = oldRechargeRate;
    }
}
