package com.gm.project.gmtool.roleTransfer.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 角色转移对象 t_role_transfer
 * 
 * @author gm
 * @date 2021-11-03
 */
public class RoleTransfer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @Excel(name = "角色ID")
    private String roleId;

    /** 原账号ID */
    @Excel(name = "原账号ID")
    private String srcUserId;

    /** 目标账号ID */
    @Excel(name = "目标账号ID")
    private String targetUserId;

    /** 服务器ID */
    @Excel(name = "服务器ID")
    private Integer serverId;

    /** 操作原因 */
    @Excel(name = "操作原因")
    private String reason;

    /** 是否删除，0 ：不删除， 1： 删除 */
    @Excel(name = "是否删除，0 ：不删除， 1： 删除")
    private Integer isDeleted;

    /** 执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "执行时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date time;

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleId()
    {
        return roleId;
    }
    public void setSrcUserId(String srcUserId)
    {
        this.srcUserId = srcUserId;
    }

    public String getSrcUserId()
    {
        return srcUserId;
    }
    public void setTargetUserId(String targetUserId)
    {
        this.targetUserId = targetUserId;
    }

    public String getTargetUserId()
    {
        return targetUserId;
    }
    public void setServerId(Integer serverId)
    {
        this.serverId = serverId;
    }

    public Integer getServerId()
    {
        return serverId;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
    public void setIsDeleted(Integer isDeleted)
    {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted()
    {
        return isDeleted;
    }
    public void setTime(Date time)
    {
        this.time = time;
    }

    public Date getTime()
    {
        return time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("srcUserId", getSrcUserId())
            .append("targetUserId", getTargetUserId())
            .append("serverId", getServerId())
            .append("reason", getReason())
            .append("isDeleted", getIsDeleted())
            .append("time", getTime())
            .toString();
    }
}
