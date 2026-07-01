package com.gm.project.stat.stat_recharge_second.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;

public class RechargeSecondTimeIntervaBean extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 付费次数
     */
    @Excel(name = "间隔天数")
    private String intervaDay;

    /**
     * 人数
     */
    @Excel(name = "人数")
    private Integer roles;


    public String getIntervaDay() {
        return intervaDay;
    }

    public void setIntervaDay(String intervaDay) {
        this.intervaDay = intervaDay;
    }

    public Integer getRoles() {
        return roles;
    }

    public void setRoles(Integer roles) {
        this.roles = roles;
    }
}
