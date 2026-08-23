package com.gm.project.gamelog.chatlog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Объект логов чата log_chatlog
 * 
 * @author gm
 * @date 2021-06-08
 */
public class Chatlog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Название платформы */
    @Excel(name = "Название платформы")
    private String platformName;

    /** id */
    private Integer id;

    /** ID персонажа */
    @Excel(name = "ID персонажа")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /** ID пользователя */
    @Excel(name = "ID пользователя")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /** Время */
    @Excel(name = "Время")
    private Long time;

    /** Имя персонажа */
    @Excel(name = "Имя персонажа")
    private String roleName;

    /** IP-адрес */
    @Excel(name = "IP-адрес")
    private String ip;

    /** Содержание чата */
    @Excel(name = "Содержание чата")
    private String content;

    /** Уровень */
    @Excel(name = "Уровень")
    private Integer level;

    /** ID получателя */
    @Excel(name = "ID получателя")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long receRoleId;

    /** Тип чата */
    @Excel(name = "Тип чата")
    private Integer channel;

    /** ID сервера */
    @Excel(name = "ID сервера")
    private Integer sid;

    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformName()
    {
        return platformName;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setTime(Long time)
    {
        this.time = time;
    }

    public Long getTime()
    {
        return time;
    }
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleName()
    {
        return roleName;
    }
    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getIp()
    {
        return ip;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Integer getLevel()
    {
        return level;
    }
    public void setReceRoleId(Long receRoleId)
    {
        this.receRoleId = receRoleId;
    }

    public Long getReceRoleId()
    {
        return receRoleId;
    }
    public void setChannel(Integer channel)
    {
        this.channel = channel;
    }

    public Integer getChannel()
    {
        return channel;
    }
    public void setSid(Integer sid)
    {
        this.sid = sid;
    }

    public Integer getSid()
    {
        return sid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("platformName", getPlatformName())
            .append("id", getId())
            .append("roleId", getRoleId())
            .append("userId", getUserId())
            .append("time", getTime())
            .append("roleName", getRoleName())
            .append("ip", getIp())
            .append("content", getContent())
            .append("level", getLevel())
            .append("receRoleId", getReceRoleId())
            .append("channel", getChannel())
            .append("sid", getSid())
            .toString();
    }
}