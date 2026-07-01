package com.gm.project.stat.stat_gold_purpose.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;


public class GoldPurposeBean {

    private static final long serialVersionUID = 1L;

    /**
     * 消耗途径
     */
    @Excel(name = "消耗途径")
    private String reason;

    @Excel(name = "消耗人数（帐号）")
    private Integer users;

    @Excel(name = "消耗数量")
    private Integer totalConsume;

    @Excel(name = "所属服务器")
    private Integer sid ;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public Integer getTotalConsume() {
        return totalConsume;
    }

    public void setTotalConsume(Integer totalConsume) {
        this.totalConsume = totalConsume;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }
}
