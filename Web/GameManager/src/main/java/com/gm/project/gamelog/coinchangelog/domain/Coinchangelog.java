package com.gm.project.gamelog.coinchangelog.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 货币变化日志对象 log_coinchangelog
 * 
 * @author gm
 * @date 2021-11-08
 */
public class Coinchangelog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /**  */
    private Integer sid;

    /** 变化前数量 */
    @Excel(name = "变化前数量")
    private Long beforeNum;

    /**  */
    private Long userId;

    /** 角色名 */
    @Excel(name = "角色名")
    private String roleName;

    /** 时间 */
    @Excel(name = "时间")
    private Long time;

    /** 角色ID */
    @Excel(name = "角色ID")
    private Long roleId;

    /** 货币类型 */
    @Excel(name = "货币类型")
    private String moneyType;

    /** 原因码 */
    @Excel(name = "原因码")
    private String reason;

    /** 玩家等级 */
    @Excel(name = "玩家等级")
    private Integer roleLevel;

    /**  */
    private String platformName;

    /** 变化后数量 */
    @Excel(name = "变化后数量")
    private Long afterNum;

    /** 关联ID */
    @Excel(name = "关联ID")
    private Long actionId;

    /** 变化数量 */
    @Excel(name = "变化数量")
    private Long changeNum;

    /** 登录ip */
    @Excel(name = "登录ip")
    private String loginIp;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setSid(Integer sid)
    {
        this.sid = sid;
    }

    public Integer getSid()
    {
        return sid;
    }
    public void setBeforeNum(Long beforeNum)
    {
        this.beforeNum = beforeNum;
    }

    public Long getBeforeNum()
    {
        return beforeNum;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleName()
    {
        return roleName;
    }
    public void setTime(Long time)
    {
        this.time = time;
    }

    public Long getTime()
    {
        return time;
    }
    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }
    public void setMoneyType(String moneyType)
    {
        this.moneyType = moneyType;
    }

    public String getMoneyType()
    {
        return moneyType;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
    public void setRoleLevel(Integer roleLevel)
    {
        this.roleLevel = roleLevel;
    }

    public Integer getRoleLevel()
    {
        return roleLevel;
    }
    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformName()
    {
        return platformName;
    }
    public void setAfterNum(Long afterNum)
    {
        this.afterNum = afterNum;
    }

    public Long getAfterNum()
    {
        return afterNum;
    }
    public void setActionId(Long actionId)
    {
        this.actionId = actionId;
    }

    public Long getActionId()
    {
        return actionId;
    }
    public void setChangeNum(Long changeNum)
    {
        this.changeNum = changeNum;
    }

    public Long getChangeNum()
    {
        return changeNum;
    }
    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public String getLoginIp()
    {
        return loginIp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sid", getSid())
            .append("beforeNum", getBeforeNum())
            .append("userId", getUserId())
            .append("roleName", getRoleName())
            .append("time", getTime())
            .append("roleId", getRoleId())
            .append("moneyType", getMoneyType())
            .append("reason", getReason())
            .append("roleLevel", getRoleLevel())
            .append("platformName", getPlatformName())
            .append("afterNum", getAfterNum())
            .append("actionId", getActionId())
            .append("changeNum", getChangeNum())
            .append("loginIp", getLoginIp())
            .toString();
    }
}
