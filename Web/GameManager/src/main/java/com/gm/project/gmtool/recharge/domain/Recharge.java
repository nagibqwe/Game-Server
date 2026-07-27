package com.gm.project.gmtool.recharge.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 后台模拟充值对象 t_recharge
 * 
 * @author gm
 * @date 2021-11-28
 */
public class Recharge extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 角色ID */
    @Excel(name = "角色ID")
    private String roleId;

    /** 充值订单金额(单位分) */
    @Excel(name = "充值订单金额(单位分)")
    private Integer rechargeNumber;

    /** 充值累积数量 */
    @Excel(name = "充值累积数量")
    private Integer rechargeTotalGold;

    /** 充值VIP经验 */
    @Excel(name = "充值VIP经验")
    private Integer rechargeVipExp;

    /** 充值状态,0为待审核,1为通过,2为失败 */
    @Excel(name = "充值状态,0为待审核,1为通过,2为失败")
    private Integer rechargeState;

    /** 操作原因 */
    @Excel(name = "操作原因")
    private String reason;

    /** 操作服务器ID */
    @Excel(name = "操作服务器ID")
    private Integer toServerId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleId()
    {
        return roleId;
    }
    public void setRechargeNumber(Integer rechargeNumber)
    {
        this.rechargeNumber = rechargeNumber;
    }

    public Integer getRechargeNumber()
    {
        return rechargeNumber;
    }
    public void setRechargeTotalGold(Integer rechargeTotalGold)
    {
        this.rechargeTotalGold = rechargeTotalGold;
    }

    public Integer getRechargeTotalGold()
    {
        return rechargeTotalGold;
    }
    public void setRechargeVipExp(Integer rechargeVipExp)
    {
        this.rechargeVipExp = rechargeVipExp;
    }

    public Integer getRechargeVipExp()
    {
        return rechargeVipExp;
    }
    public void setRechargeState(Integer rechargeState)
    {
        this.rechargeState = rechargeState;
    }

    public Integer getRechargeState()
    {
        return rechargeState;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
    public void setToServerId(Integer toServerId)
    {
        this.toServerId = toServerId;
    }

    public Integer getToServerId()
    {
        return toServerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("roleId", getRoleId())
            .append("rechargeNumber", getRechargeNumber())
            .append("rechargeTotalGold", getRechargeTotalGold())
            .append("rechargeVipExp", getRechargeVipExp())
            .append("rechargeState", getRechargeState())
            .append("reason", getReason())
            .append("toServerId", getToServerId())
            .toString();
    }
}
