package com.gm.project.stat.stat_recharge_second.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;

public class RechargeSecondItemBean {

    private static final long serialVersionUID = 1L;

    /**
     * 付费次数
     */
    @Excel(name = "二次付费项目")
    private String secondItem;

    /**
     * 人数
     */
    @Excel(name = "人数")
    private Integer roles;

    public String getSecondItem() {
        return secondItem;
    }

    public void setSecondItem(String secondItem) {
        this.secondItem = secondItem;
    }

    public Integer getRoles() {
        return roles;
    }

    public void setRoles(Integer roles) {
        this.roles = roles;
    }
}
