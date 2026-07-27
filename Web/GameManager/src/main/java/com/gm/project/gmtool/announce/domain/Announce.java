package com.gm.project.gmtool.announce.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 即时公告对象 t_announce
 * 
 * @author gm
 * @date 2021-10-21
 */
public class Announce
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 公告创建时间 */
    @Excel(name = "公告创建时间")
    private Long createTime;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createDate;

    /** 创建者账号ID */
    @Excel(name = "创建者账号ID")
    private Integer userId;

    /** 创建者 */
    @Excel(name = "创建者")
    private String userName;

    /** 服务器组 */
    @Excel(name = "服务器组")
    private String groupName;

    /** 服务器id */
    @Excel(name = "服务器id")
    private String serverIds;

    /** 类型 */
    @Excel(name = "类型")
    private Integer type;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 原因 */
    @Excel(name = "原因")
    private String reason;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getCreateDate()
    {
        return createDate;
    }
    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Integer getUserId()
    {
        return userId;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }
    public void setServerIds(String serverIds)
    {
        this.serverIds = serverIds;
    }

    public String getServerIds()
    {
        return serverIds;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
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
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("createTime", getCreateTime())
            .append("createDate", getCreateDate())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("groupName", getGroupName())
            .append("serverIds", getServerIds())
            .append("type", getType())
            .append("content", getContent())
            .append("reason", getReason())
            .toString();
    }
}
