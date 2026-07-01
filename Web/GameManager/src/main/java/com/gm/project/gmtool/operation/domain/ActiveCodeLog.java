package com.gm.project.gmtool.operation.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 激活码日志对象 activecodelog
 * 
 * @author gm
 * @date 2021-09-18
 */
public class ActiveCodeLog
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 激活码 */
    @Excel(name = "激活码")
    private String activeCode;

    /** 平台名 */
    @Excel(name = "平台名")
    private String platformName;

    /** 区服 */
    @Excel(name = "区服")
    private String sid;

    /** 角色ID */
    @Excel(name = "角色ID")
    private String roleid;

    /** 账号ID */
    @Excel(name = "账号ID")
    private String userId;

    /** 物品列表 */
    @Excel(name = "物品列表")
    private String itemList;

    /** 唯一标识 */
    @Excel(name = "唯一标识")
    private String actionId;

    /** 时间 */
    @Excel(name = "时间")
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
