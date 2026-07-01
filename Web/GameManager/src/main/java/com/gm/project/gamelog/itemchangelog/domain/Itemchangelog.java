package com.gm.project.gamelog.itemchangelog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 物品变化日志对象 log_itemchangelog
 * 
 * @author gm
 * @date 2021-09-09
 */
public class Itemchangelog extends BaseEntity
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

    /** 等级 */
    @Excel(name = "等级")
    private Integer roleLevel;

    /** 角色名 */
    @Excel(name = "角色名")
    private String roleName;

    /** 物品ID */
    @Excel(name = "物品ID")
    private Long itemId;

    /** 道具ID */
    @Excel(name = "道具ID")
    private String modelId;

    /** 变化数量 */
    @Excel(name = "变化数量")
    private Integer changeNum;

    /** 变化前数量 */
    @Excel(name = "变化前数量")
    private Integer oldNum;

    /** 变化后数量 */
    @Excel(name = "变化后数量")
    private Integer newNum;

    /** 原因码 */
    @Excel(name = "原因码")
    private String reason;

    /** 关联ID */
    @Excel(name = "关联ID")
    private Long actionId;

    /** 变化类型 */
    @Excel(name = "变化类型")
    private String changeAction;

    /** 消耗货币类型 */
    @Excel(name = "消耗货币类型")
    private Integer coinType;

    /** costNum */
    @Excel(name = "costNum")
    private Double costNum;

    /**  */
    private Long sid;

    /**  */
    private String platformName;

    /**  */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**  */
    private Integer cellId;

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
    public void setRoleLevel(Integer roleLevel)
    {
        this.roleLevel = roleLevel;
    }

    public Integer getRoleLevel()
    {
        return roleLevel;
    }
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleName()
    {
        return roleName;
    }
    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getItemId()
    {
        return itemId;
    }
    public void setModelId(String modelId)
    {
        this.modelId = modelId;
    }

    public String getModelId()
    {
        return modelId;
    }
    public void setChangeNum(Integer changeNum)
    {
        this.changeNum = changeNum;
    }

    public Integer getChangeNum()
    {
        return changeNum;
    }
    public void setOldNum(Integer oldNum)
    {
        this.oldNum = oldNum;
    }

    public Integer getOldNum()
    {
        return oldNum;
    }
    public void setNewNum(Integer newNum)
    {
        this.newNum = newNum;
    }

    public Integer getNewNum()
    {
        return newNum;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
    public void setActionId(Long actionId)
    {
        this.actionId = actionId;
    }

    public Long getActionId()
    {
        return actionId;
    }
    public void setChangeAction(String changeAction)
    {
        this.changeAction = changeAction;
    }

    public String getChangeAction()
    {
        return changeAction;
    }
    public void setCoinType(Integer coinType)
    {
        this.coinType = coinType;
    }

    public Integer getCoinType()
    {
        return coinType;
    }
    public void setCostNum(Double costNum)
    {
        this.costNum = costNum;
    }

    public Double getCostNum()
    {
        return costNum;
    }
    public void setSid(Long sid)
    {
        this.sid = sid;
    }

    public Long getSid()
    {
        return sid;
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
    public void setCellId(Integer cellId)
    {
        this.cellId = cellId;
    }

    public Integer getCellId()
    {
        return cellId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("time", getTime())
            .append("roleId", getRoleId())
            .append("roleLevel", getRoleLevel())
            .append("roleName", getRoleName())
            .append("itemId", getItemId())
            .append("modelId", getModelId())
            .append("changeNum", getChangeNum())
            .append("oldNum", getOldNum())
            .append("newNum", getNewNum())
            .append("reason", getReason())
            .append("actionId", getActionId())
            .append("changeAction", getChangeAction())
            .append("coinType", getCoinType())
            .append("costNum", getCostNum())
            .append("sid", getSid())
            .append("platformName", getPlatformName())
            .append("userId", getUserId())
            .append("cellId", getCellId())
            .toString();
    }
}
