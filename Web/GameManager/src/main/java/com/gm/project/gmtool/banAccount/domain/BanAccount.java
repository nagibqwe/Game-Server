package com.gm.project.gmtool.banAccount.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Блокировка аккаунта对象 t_ban_account
 * 
 * @author gm
 * @date 2021-11-21
 */
public class BanAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 封号的条件 */
    @Excel(name = "封号的条件")
    private String con;

    /** Окончание блокировки */
    @Excel(name = "Окончание блокировки")
    private String endTime;

    /** Статус: 0 — заблокирован, 1 — разблокирован */
    @Excel(name = "Статус: 0 — заблокирован, 1 — разблокирован")
    private Integer state;

    /** Причина */
    @Excel(name = "Причина")
    private String reason;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCon(String con)
    {
        this.con = con;
    }

    public String getCon()
    {
        return con;
    }
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getEndTime()
    {
        return endTime;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getState()
    {
        return state;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("con", getCon())
            .append("endTime", getEndTime())
            .append("state", getState())
            .append("reason", getReason())
            .toString();
    }
}
