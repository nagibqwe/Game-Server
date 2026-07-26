package com.gm.project.gmtool.operation.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Код активацииЖурнал对象 activecodelog
 * 
 * @author gm
 * @date 2021-09-18
 */
public class ActiveCodeLog
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** Код активации */
    @Excel(name = "Код активации")
    private String activeCode;

    /** Платформа */
    @Excel(name = "Платформа")
    private String platformName;

    /** Игровой сервер */
    @Excel(name = "Игровой сервер")
    private String sid;

    /** ID персонажа */
    @Excel(name = "ID персонажа")
    private String roleid;

    /** ID аккаунта */
    @Excel(name = "ID аккаунта")
    private String userId;

    /** Список предметов */
    @Excel(name = "Список предметов")
    private String itemList;

    /** 唯一标识 */
    @Excel(name = "唯一标识")
    private String actionId;

    /** Время */
    @Excel(name = "Время")
    private Long time;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setActiveCode(String activeCode)
    {
        this.activeCode = activeCode;
    }

    public String getActiveCode()
    {
        return activeCode;
    }
    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformName()
    {
        return platformName;
    }
    public void setSid(String sid)
    {
        this.sid = sid;
    }

    public String getSid()
    {
        return sid;
    }
    public void setRoleid(String roleid)
    {
        this.roleid = roleid;
    }

    public String getRoleid()
    {
        return roleid;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }
    public void setItemList(String itemList)
    {
        this.itemList = itemList;
    }

    public String getItemList()
    {
        return itemList;
    }
    public void setActionId(String actionId)
    {
        this.actionId = actionId;
    }

    public String getActionId()
    {
        return actionId;
    }
    public void setTime(Long time)
    {
        this.time = time;
    }

    public Long getTime()
    {
        return time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("activeCode", getActiveCode())
            .append("platformName", getPlatformName())
            .append("sid", getSid())
            .append("roleid", getRoleid())
            .append("userId", getUserId())
            .append("itemList", getItemList())
            .append("actionId", getActionId())
            .append("time", getTime())
            .toString();
    }
}
