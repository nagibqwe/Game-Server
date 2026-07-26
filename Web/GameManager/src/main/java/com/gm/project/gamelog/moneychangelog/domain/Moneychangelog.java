package com.gm.project.gamelog.moneychangelog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Журнал изменения валюты对象 log_moneychangelog
 * 
 * @author gm
 * @date 2021-09-09
 */
public class Moneychangelog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** Время */
    @Excel(name = "Время")
    private Long time;

    /** Связанный ID */
    @Excel(name = "Связанный ID")
    private Long actionId;

    /** ID персонажа */
    @Excel(name = "ID персонажа")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /** Имя персонажа */
    @Excel(name = "Имя персонажа")
    private String roleName;

    /** Уровень персонажа */
    @Excel(name = "Уровень персонажа")
    private Integer roleLevel;

    /** Тип валюты */
    @Excel(name = "Тип валюты")
    private Integer monyeyType;

    /** Изменение количества */
    @Excel(name = "Изменение количества")
    private Long changeNum;

    /** Количество до */
    @Excel(name = "Количество до")
    private Long beforeNum;

    /** Количество после */
    @Excel(name = "Количество после")
    private Long afterNum;

    /** Код причины */
    @Excel(name = "Код причины")
    private Integer reason;

    /** IP входа */
    @Excel(name = "IP входа")
    private String loginIp;

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
    public void setMonyeyType(Integer monyeyType)
    {
        this.monyeyType = monyeyType;
    }

    public Integer getMonyeyType()
    {
        return monyeyType;
    }
    public void setChangeNum(Long changeNum)
    {
        this.changeNum = changeNum;
    }

    public Long getChangeNum()
    {
        return changeNum;
    }
    public void setBeforeNum(Long beforeNum)
    {
        this.beforeNum = beforeNum;
    }

    public Long getBeforeNum()
    {
        return beforeNum;
    }
    public void setAfterNum(Long afterNum)
    {
        this.afterNum = afterNum;
    }

    public Long getAfterNum()
    {
        return afterNum;
    }
    public void setReason(Integer reason)
    {
        this.reason = reason;
    }

    public Integer getReason()
    {
        return reason;
    }
    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public String getLoginIp()
    {
        return loginIp;
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
            .append("monyeyType", getMonyeyType())
            .append("changeNum", getChangeNum())
            .append("beforeNum", getBeforeNum())
            .append("afterNum", getAfterNum())
            .append("reason", getReason())
            .append("loginIp", getLoginIp())
            .append("platformName", getPlatformName())
            .append("userId", getUserId())
            .append("sid", getSid())
            .toString();
    }
}
