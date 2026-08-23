package com.gm.project.gamelog.changerolenamelog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Объект логов смены имени log_changerolenamelog
 * 
 * @author gm
 * @date 2021-09-09
 */
public class Changerolenamelog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** Время */
    @Excel(name = "Время")
    private Long time;

    /** ID персонажа */
    @Excel(name = "ID персонажа")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long playerId;

    /** ID аккаунта */
    @Excel(name = "ID аккаунта")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /** Сервер */
    @Excel(name = "Сервер")
    private Long sid;

    /** Имя персонажа до смены */
    @Excel(name = "Имя до смены")
    private String oldName;

    /** Имя персонажа после смены */
    @Excel(name = "Имя после смены")
    private String newName;

    /** ID предмета */
    @Excel(name = "ID предмета")
    private Long modelId;

    /** Название платформы */
    @Excel(name = "Название платформы")
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