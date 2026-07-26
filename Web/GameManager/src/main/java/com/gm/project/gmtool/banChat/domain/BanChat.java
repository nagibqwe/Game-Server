package com.gm.project.gmtool.banChat.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 聊День封禁对象 t_ban_chat
 * 
 * @author gm
 * @date 2021-11-20
 */
public class BanChat extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 聊День禁言的账号 */
    @Excel(name = "聊День禁言的账号")
    private String userId;

    /** 违规Тип 1黑色产业 2不良Информация */
    @Excel(name = "违规Тип 1黑色产业 2不良Информация")
    private Integer crimeType;

    /** 禁言Тип 1:工作室禁言2:全文替换禁言3:关键字替换禁言4:常规禁言5:隐形禁言6:隔离禁言 */
    @Excel(name = "禁言Тип 1:工作室禁言2:全文替换禁言3:关键字替换禁言4:常规禁言5:隐形禁言6:隔离禁言")
    private Integer banType;

    /** Окончание блокировки */
    @Excel(name = "Окончание блокировки")
    private String endTime;

    /** Причина */
    @Excel(name = "Причина")
    private String reason;

    /** 发送到游戏服列表 */
    @Excel(name = "发送到游戏服列表")
    private String serverIds;

    /** Статус: 0 — заблокирован, 1 — разблокирован */
    @Excel(name = "Статус: 0 — заблокирован, 1 — разблокирован")
    private Integer state;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }
    public void setCrimeType(Integer crimeType)
    {
        this.crimeType = crimeType;
    }

    public Integer getCrimeType()
    {
        return crimeType;
    }
    public void setBanType(Integer banType)
    {
        this.banType = banType;
    }

    public Integer getBanType()
    {
        return banType;
    }
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getEndTime()
    {
        return endTime;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
    public void setServerIds(String serverIds)
    {
        this.serverIds = serverIds;
    }

    public String getServerIds()
    {
        return serverIds;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getState()
    {
        return state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("crimeType", getCrimeType())
            .append("banType", getBanType())
            .append("endTime", getEndTime())
            .append("reason", getReason())
            .append("serverIds", getServerIds())
            .append("state", getState())
            .toString();
    }
}
