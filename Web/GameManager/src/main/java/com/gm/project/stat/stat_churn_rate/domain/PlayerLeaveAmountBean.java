package com.gm.project.stat.stat_churn_rate.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;

public class PlayerLeaveAmountBean {
    @Excel(name = "累计金额")
    private String key;
    @Excel(name = "账号数")
    private int paylcount;
    @Excel(name = "流失用户充值总金额")
    private int rechargeSum;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPaylcount() {
        return paylcount;
    }

    public void setPaylcount(int paylcount) {
        this.paylcount = paylcount;
    }

    public int getRechargeSum() {
        return rechargeSum;
    }

    public void setRechargeSum(int rechargeSum) {
        this.rechargeSum = rechargeSum;
    }
}
