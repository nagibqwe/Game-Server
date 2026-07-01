package com.gm.project.gamelog.chatlog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 聊天日志对象 log_chatlog
 * 
 * @author gm
 * @date 2021-06-08
 */
public class Chatlog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 平台名称 */
    @Excel(name = "平台名称")
    private String platformName;

    /** id */
    private Integer id;

    /** 角色ID */
    @Excel(name = "角色ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /** 用户id */
    @Excel(name = "用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /** 时间 */
    @Excel(name = "时间")
    private Long time;

    /** 角色名 */
    @Excel(name = "角色名")
    private String roleName;

    /** ip */
    @Excel(name = "ip")
    private String ip;

    /** 聊天内容 */
    @Excel(name = "聊天内容")
    private String content;

    /** 等级 */
    @Excel(name = "等级")
    private Integer level;

    /** 接收角色ID */
    @Excel(name = "接收角色ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long receRoleId;

    /** 聊天类型 */
    @Excel(name = "聊天类型")
    private Integer channel;

    /** 服务器id */
    @Excel(name = "服务器id")
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
