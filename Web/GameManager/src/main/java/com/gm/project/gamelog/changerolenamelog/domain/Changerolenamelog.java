package com.gm.project.gamelog.changerolenamelog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 改名日志对象 log_changerolenamelog
 * 
 * @author gm
 * @date 2021-09-09
 */
public class Changerolenamelog extends BaseEntity
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
    private Long playerId;

    /** 账号ID */
    @Excel(name = "账号ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /** 区服 */
    @Excel(name = "区服")
    private Long sid;

    /** 改名前角色名 */
    @Excel(name = "改名前角色名")
    private String oldName;

    /** 改名后角色名 */
    @Excel(name = "改名后角色名")
    private String newName;

    /** 道具ID */
    @Excel(name = "道具ID")
    private Long modelId;

    /** 平台名字 */
    @Excel(name = "平台名字")
    private String platformName;

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
    public void setPlayerId(Long playerId)
    {
        this.playerId = playerId;
    }

    public Long getPlayerId()
    {
        return playerId;
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
    public void setOldName(String oldName)
    {
        this.oldName = oldName;
    }

    public String getOldName()
    {
        return oldName;
    }
    public void setNewName(String newName)
    {
        this.newName = newName;
    }

    public String getNewName()
    {
        return newName;
    }
    public void setModelId(Long modelId)
    {
        this.modelId = modelId;
    }

    public Long getModelId()
    {
        return modelId;
    }
    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformName()
    {
        return platformName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("time", getTime())
            .append("playerId", getPlayerId())
            .append("userId", getUserId())
            .append("sid", getSid())
            .append("oldName", getOldName())
            .append("newName", getNewName())
            .append("modelId", getModelId())
            .append("platformName", getPlatformName())
            .toString();
    }
}
