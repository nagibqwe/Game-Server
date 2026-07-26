package com.gm.project.gamelog.guildbaselog.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 公会基础Информация对象 log_guildbaselog
 * 
 * @author gm
 * @date 2021-12-06
 */
public class Guildbaselog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** ID гильдии */
    @Excel(name = "ID гильдии")
    private Long guildId;

    /** ID персонажа */
    @Excel(name = "ID персонажа")
    private Long roleId;

    /** Название канала */
    @Excel(name = "Название канала")
    private String platformName;

    /** Дополнительные параметры */
    @Excel(name = "Дополнительные параметры")
    private String param;

    /** ID сервера */
    @Excel(name = "ID сервера")
    private Integer sid;

    /** Название гильдии */
    @Excel(name = "Название гильдии")
    private String guildName;

    /** Имя персонажа */
    @Excel(name = "Имя персонажа")
    private String roleName;

    /** ID пользователя */
    @Excel(name = "ID пользователя")
    private Long userId;

    /** Действия唯一Номер */
    @Excel(name = "Действия唯一Номер")
    private Long actionId;

    /** 记录Время */
    @Excel(name = "记录Время")
    private Long time;

    /** 被ДействияID персонажа */
    @Excel(name = "被ДействияID персонажа")
    private Long actId;

    /** 被ДействияИмя персонажа */
    @Excel(name = "被ДействияИмя персонажа")
    private String actName;

    /** ДействияТип 1:公会创建2:公会解散3:加入公会4:主动退会5:被踢出公会6：公会Информация设置7：公会改名8：开始弹劾会长 9:Отмена弹劾会长 10:弹劾会长Успешно */
    @Excel(name = "ДействияТип 1:公会创建2:公会解散3:加入公会4:主动退会5:被踢出公会6：公会Информация设置7：公会改名8：开始弹劾会长 9:Отмена弹劾会长 10:弹劾会长Успешно")
    private Integer type;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setGuildId(Long guildId)
    {
        this.guildId = guildId;
    }

    public Long getGuildId()
    {
        return guildId;
    }
    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }
    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformName()
    {
        return platformName;
    }
    public void setParam(String param)
    {
        this.param = param;
    }

    public String getParam()
    {
        return param;
    }
    public void setSid(Integer sid)
    {
        this.sid = sid;
    }

    public Integer getSid()
    {
        return sid;
    }
    public void setGuildName(String guildName)
    {
        this.guildName = guildName;
    }

    public String getGuildName()
    {
        return guildName;
    }
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleName()
    {
        return roleName;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setActionId(Long actionId)
    {
        this.actionId = actionId;
    }

    public Long getActionId()
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
    public void setActId(Long actId)
    {
        this.actId = actId;
    }

    public Long getActId()
    {
        return actId;
    }
    public void setActName(String actName)
    {
        this.actName = actName;
    }

    public String getActName()
    {
        return actName;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("guildId", getGuildId())
            .append("roleId", getRoleId())
            .append("platformName", getPlatformName())
            .append("param", getParam())
            .append("sid", getSid())
            .append("guildName", getGuildName())
            .append("roleName", getRoleName())
            .append("userId", getUserId())
            .append("actionId", getActionId())
            .append("time", getTime())
            .append("actId", getActId())
            .append("actName", getActName())
            .append("type", getType())
            .toString();
    }
}
