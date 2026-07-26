package com.gm.project.gamelog.coinchangelog.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Журнал изменения валюты对象 log_coinchangelog
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

    /** Количество до */
    @Excel(name = "Количество до")
    private Long beforeNum;

    /**  */
    private Long userId;

    /** Имя персонажа */
    @Excel(name = "Имя персонажа")
    private String roleName;

    /** Время */
    @Excel(name = "Время")
    private Long time;

    /** ID персонажа */
    @Excel(name = "ID персонажа")
    private Long roleId;

    /** Тип валюты */
    @Excel(name = "Тип валюты")
    private String moneyType;

    /** Код причины */
    @Excel(name = "Код причины")
    private String reason;

    /** 玩家Уровень */
    @Excel(name = "玩家Уровень")
    private Integer roleLevel;

    /**  */
    private String platformName;

    /** Количество после */
    @Excel(name = "Количество после")
    private Long afterNum;

    /** Связанный ID */
    @Excel(name = "Связанный ID")
    private Long actionId;

    /** Изменение количества */
    @Excel(name = "Изменение количества")
    private Long changeNum;

    /** IP входа */
    @Excel(name = "IP входа")
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
