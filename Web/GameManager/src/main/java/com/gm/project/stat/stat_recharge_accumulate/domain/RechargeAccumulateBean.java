package com.gm.project.stat.stat_recharge_accumulate.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;

public class RechargeAccumulateBean extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 等级 */
    @Excel(name = "日期")
    private int amount;
    /**
     * 累充金额
     */
    @Excel(name = "累充金额")
    private String accumulateAmount;
    /**
     * 人数
     */
    @Excel(name = "人数")
    private int roleNum;
    /**
     * 充值金额
     */
    @Excel(name = "充值金额")
    private float rechargeAmount;


    @Excel(name="用户占比")
    private String roleNumRate;

    public void setAccumulateAmount(String accumulateAmount) {
        this.accumulateAmount = accumulateAmount;
    }

    public int getRoleNum() {
        return roleNum;
    }

    public void setRoleNum(int roleNum) {
        this.roleNum = roleNum;
    }


    public float getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(float rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getAccumulateAmount() {
        return accumulateAmount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getRoleNumRate() {
        return roleNumRate;
    }

    public void setRoleNumRate(String roleNumRate) {
        this.roleNumRate = roleNumRate;
    }
}
