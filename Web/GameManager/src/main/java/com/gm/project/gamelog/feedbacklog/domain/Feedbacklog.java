package com.gm.project.gamelog.feedbacklog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 反馈日志对象 log_feedbacklog
 * 
 * @author gm
 * @date 2021-09-10
 */
public class Feedbacklog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 时间 */
    @Excel(name = "时间")
    private Long time;

    /** 角色ID */
    @Excel(name = "角色ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /** 角色名 */
    @Excel(name = "角色名")
    private String roleName;

    /** sender */
    @Excel(name = "sender")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sender;

    /** type */
    @Excel(name = "type")
    private Long type;

    /** content */
    @Excel(name = "content")
    private String content;

    /** sendTime */
    @Excel(name = "sendTime")
    private Long sendTime;

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
    public void setSender(Long sender)
    {
        this.sender = sender;
    }

    public Long getSender()
    {
        return sender;
    }
    public void setType(Long type)
    {
        this.type = type;
    }

    public Long getType()
    {
        return type;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setSendTime(Long sendTime)
    {
        this.sendTime = sendTime;
    }

    public Long getSendTime()
    {
        return sendTime;
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
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("sender", getSender())
            .append("type", getType())
            .append("content", getContent())
            .append("sendTime", getSendTime())
            .append("platformName", getPlatformName())
            .append("userId", getUserId())
            .append("sid", getSid())
            .toString();
    }
}
