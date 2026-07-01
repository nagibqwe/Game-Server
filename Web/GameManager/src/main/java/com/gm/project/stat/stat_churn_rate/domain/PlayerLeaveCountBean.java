package com.gm.project.stat.stat_churn_rate.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;

public class PlayerLeaveCountBean extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name = "时间")
    private String date;
    private int roleLoginCount;
    private int roleLostCount;
    private float rate;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRoleLoginCount() {
        return roleLoginCount;
    }

    public void setRoleLoginCount(int roleLoginCount) {
        this.roleLoginCount = roleLoginCount;
    }

    public int getRoleLostCount() {
        return roleLostCount;
    }

    public void setRoleLostCount(int roleLostCount) {
        this.roleLostCount = roleLostCount;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
