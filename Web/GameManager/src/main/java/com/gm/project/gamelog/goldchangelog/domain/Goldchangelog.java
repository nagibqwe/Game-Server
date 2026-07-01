package com.gm.project.gamelog.goldchangelog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 元宝变化日志对象 log_goldchangelog
 * 
 * @author gm
 * @date 2021-09-11
 */
public class Goldchangelog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 时间 */
    @Excel(name = "时间")
    private Long time;

    /** 关联ID */
    @Excel(name = "关联ID")
    private Long actionId;

    /** 角色ID */
    @Excel(name = "角色ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /** 角色名 */
    @Excel(name = "角色名")
    private String roleName;

    /** 角色等级 */
    @Excel(name = "角色等级")
    private Integer roleLevel;

    /** 登录ip */
    @Excel(name = "登录ip")
    private String loginIp;

    /** 变化量 */
    @Excel(name = "变化量")
    private Integer changeNum;

    /** 变化前元宝数 */
    @Excel(name = "变化前元宝数")
    private Integer beforeNum;

    /** 变化后元宝数 */
    @Excel(name = "变化后元宝数")
    private Integer afterNum;

    /** 原因码 */
    @Excel(name = "原因码")
    private String reason;

    /**  */
    private String platformName;

    /**  */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**  */
    private Long sid;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTime(Long time)
    {
        this.time = time;
    }

    public Long getTime()
    {
        return time;
    }
    public void setActionId(Long actionId)
    {
        this.actionId = actionId;
    }

    public Long getActionId()
    {
        return actionId;
    }
    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleName()
    {
        return roleName;
    }
    public void setRoleLevel(Integer roleLevel)
    {
        this.roleLevel = roleLevel;
    }

    public Integer getRoleLevel()
    {
        return roleLevel;
    }
    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public String getLoginIp()
    {
        return loginIp;
    }
    public void setChangeNum(Integer changeNum)
    {
        this.changeNum = changeNum;
    }

    public Integer getChangeNum()
    {
        return changeNum;
    }
    public void setBeforeNum(Integer beforeNum)
    {
        this.beforeNum = beforeNum;
    }

    public Integer getBeforeNum()
    {
        return beforeNum;
    }
    public void setAfterNum(Integer afterNum)
    {
        this.afterNum = afterNum;
    }

    public Integer getAfterNum()
    {
        return afterNum;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformName()
    {
        return platformName;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setSid(Long sid)
    {
        this.sid = sid;
    }

    public Long getSid()
    {
        return sid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("time", getTime())
            .append("actionId", getActionId())
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("roleLevel", getRoleLevel())
            .append("loginIp", getLoginIp())
            .append("changeNum", getChangeNum())
            .append("beforeNum", getBeforeNum())
            .append("afterNum", getAfterNum())
            .append("reason", getReason())
            .append("platformName", getPlatformName())
            .append("userId", getUserId())
            .append("sid", getSid())
            .toString();
    }
}
