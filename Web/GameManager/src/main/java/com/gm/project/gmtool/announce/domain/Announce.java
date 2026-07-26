package com.gm.project.gmtool.announce.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Мгновенное объявление对象 t_announce
 * 
 * @author gm
 * @date 2021-10-21
 */
public class Announce
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 公告Время создания */
    @Excel(name = "公告Время создания")
    private Long createTime;

    /** Время создания */
    @Excel(name = "Время создания")
    private String createDate;

    /** СоздательID аккаунта */
    @Excel(name = "СоздательID аккаунта")
    private Integer userId;

    /** Создатель */
    @Excel(name = "Создатель")
    private String userName;

    /** Сервер组 */
    @Excel(name = "Сервер组")
    private String groupName;

    /** ID сервера */
    @Excel(name = "ID сервера")
    private String serverIds;

    /** Тип */
    @Excel(name = "Тип")
    private Integer type;

    /** Содержимое */
    @Excel(name = "Содержимое")
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
