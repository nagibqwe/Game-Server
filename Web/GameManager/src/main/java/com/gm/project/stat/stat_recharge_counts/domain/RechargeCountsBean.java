package com.gm.project.stat.stat_recharge_counts.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;

public class RechargeCountsBean extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 付费次数
     */
    @Excel(name = "付费次数")
    private String rechargeTimes;
    /**
     * 人数
     */
    @Excel(name = "人数")
    private Float rechargeRoles;
    /**
     * 付费总额
     */
    @Excel(name = "付费总额")
    private Float rechargeAmounts;

    public String getRechargeTimes() {
        return rechargeTimes;
    }

    public void setRechargeTimes(String rechargeTimes) {
        this.rechargeTimes = rechargeTimes;
    }

    public Float getRechargeRoles() {
        return rechargeRoles;
    }

    public void setRechargeRoles(Float rechargeRoles) {
        this.rechargeRoles = rechargeRoles;
    }

    public Float getRechargeAmounts() {
        return rechargeAmounts;
    }

    public void setRechargeAmounts(Float rechargeAmounts) {
        this.rechargeAmounts = rechargeAmounts;
    }
}
