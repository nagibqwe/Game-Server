package com.gm.project.stat.stat_recharge_accumulate.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;

public class RechargeAccumulateBean extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Уровень */
    @Excel(name = "Дата")
    private int amount;
    /**
     * 累充Сумма
     */
    @Excel(name = "累充Сумма")
    private String accumulateAmount;
    /**
     * Количество
     */
    @Excel(name = "Количество")
    private int roleNum;
    /**
     * Сумма пополнения
     */
    @Excel(name = "Сумма пополнения")
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
