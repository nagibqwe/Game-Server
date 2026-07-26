package com.gm.project.gmtool.roleAttr.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Изменить характеристики对象 t_role_attr
 * 
 * @author gm
 * @date 2021-11-02
 */
public class RoleAttr extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 属性设置ID */
    private Integer id;

    /** ID сервера */
    @Excel(name = "ID сервера")
    private Integer serverId;

    /** ID персонажа */
    @Excel(name = "ID персонажа")
    private String roleId;

    /** 属性Тип */
    @Excel(name = "属性Тип")
    private Integer attrType;

    /** 设置的属性值 */
    @Excel(name = "设置的属性值")
    private Integer attrValue;

    /** 真实的属性值 */
    @Excel(name = "真实的属性值")
    private Integer realValue;

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
    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleId()
    {
        return roleId;
    }
    public void setAttrType(Integer attrType)
    {
        this.attrType = attrType;
    }

    public Integer getAttrType()
    {
        return attrType;
    }
    public void setAttrValue(Integer attrValue)
    {
        this.attrValue = attrValue;
    }

    public Integer getAttrValue()
    {
        return attrValue;
    }
    public void setRealValue(Integer realValue)
    {
        this.realValue = realValue;
    }

    public Integer getRealValue()
    {
        return realValue;
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
            .append("roleId", getRoleId())
            .append("attrType", getAttrType())
            .append("attrValue", getAttrValue())
            .append("realValue", getRealValue())
            .append("actionTime", getActionTime())
            .append("reason", getReason())
            .append("isDelete", getIsDelete())
            .append("actionUser", getActionUser())
            .toString();
    }
}
