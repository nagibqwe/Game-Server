package com.gm.project.gamelog.gmcommandlog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * gm命令Журнал对象 log_gmcommandlog
 * 
 * @author gm
 * @date 2021-09-08
 */
public class Gmcommandlog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** Время */
    @Excel(name = "Время")
    private Long time;

    /** ID пользователя */
    @Excel(name = "ID пользователя")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /** ID персонажа */
    @Excel(name = "ID персонажа")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /** Имя персонажа */
    @Excel(name = "Имя персонажа")
    private String roleName;

    /** ID сервера */
    @Excel(name = "ID сервера")
    private Integer sid;

    /** gmУровень */
    @Excel(name = "gmУровень")
    private Integer gmLevel;

    /** 命令Содержимое */
    @Excel(name = "命令Содержимое")
    private String command;

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
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
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
    public void setSid(Integer sid)
    {
        this.sid = sid;
    }

    public Integer getSid()
    {
        return sid;
    }
    public void setGmLevel(Integer gmLevel)
    {
        this.gmLevel = gmLevel;
    }

    public Integer getGmLevel()
    {
        return gmLevel;
    }
    public void setCommand(String command)
    {
        this.command = command;
    }

    public String getCommand()
    {
        return command;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("time", getTime())
            .append("userId", getUserId())
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("sid", getSid())
            .append("gmLevel", getGmLevel())
            .append("command", getCommand())
            .toString();
    }
}
