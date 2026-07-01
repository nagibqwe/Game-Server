package com.gm.project.gamelog.gmcommandlog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * gm命令日志对象 log_gmcommandlog
 * 
 * @author gm
 * @date 2021-09-08
 */
public class Gmcommandlog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 时间 */
    @Excel(name = "时间")
    private Long time;

    /** 用户id */
    @Excel(name = "用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /** 角色id */
    @Excel(name = "角色id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /** 角色名称 */
    @Excel(name = "角色名称")
    private String roleName;

    /** 服务器id */
    @Excel(name = "服务器id")
    private Integer sid;

    /** gm等级 */
    @Excel(name = "gm等级")
    private Integer gmLevel;

    /** 命令内容 */
    @Excel(name = "命令内容")
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
