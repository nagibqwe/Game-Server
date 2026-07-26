package com.gm.project.gmtool.evaluate.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Оценки включены对象 t_evaluate
 * 
 * @author gm
 * @date 2021-11-04
 */
public class Evaluate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评价ID */
    private Integer id;

    /** ID сервера */
    @Excel(name = "ID сервера")
    private Integer serverId;

    /** 评价Тип */
    @Excel(name = "评价Тип")
    private Integer eType;

    /** 开关Статус */
    @Excel(name = "开关Статус")
    private Integer state;

    /** Время выполнения */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Время выполнения", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actionTime;

    /** 设置原因 */
    @Excel(name = "设置原因")
    private String reason;

    /** Удалён，0 ：不Удалить， 1： Удалить */
    @Excel(name = "Удалён，0 ：不Удалить， 1： Удалить")
    private Integer isDelete;

    /** Имя оператора */
    @Excel(name = "Имя оператора")
    private String actionUser;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setServerId(Integer serverId)
    {
        this.serverId = serverId;
    }

    public Integer getServerId()
    {
        return serverId;
    }
    public void seteType(Integer eType)
    {
        this.eType = eType;
    }

    public Integer geteType()
    {
        return eType;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getState()
    {
        return state;
    }
    public void setActionTime(Date actionTime)
    {
        this.actionTime = actionTime;
    }

    public Date getActionTime()
    {
        return actionTime;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }
    public void setActionUser(String actionUser)
    {
        this.actionUser = actionUser;
    }

    public String getActionUser()
    {
        return actionUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverId", getServerId())
            .append("eType", geteType())
            .append("state", getState())
            .append("actionTime", getActionTime())
            .append("reason", getReason())
            .append("isDelete", getIsDelete())
            .append("actionUser", getActionUser())
            .toString();
    }
}
