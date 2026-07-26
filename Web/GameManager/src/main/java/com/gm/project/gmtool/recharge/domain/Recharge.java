package com.gm.project.gmtool.recharge.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Тестовое пополнение через GM对象 t_recharge
 * 
 * @author gm
 * @date 2021-11-28
 */
public class Recharge extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** ID персонажа */
    @Excel(name = "ID персонажа")
    private String roleId;

    /** ПополнениеЗаказыСумма(单位分) */
    @Excel(name = "ПополнениеЗаказыСумма(单位分)")
    private Integer rechargeNumber;

    /** Пополнение累积数量 */
    @Excel(name = "Пополнение累积数量")
    private Integer rechargeTotalGold;

    /** ПополнениеVIP经验 */
    @Excel(name = "ПополнениеVIP经验")
    private Integer rechargeVipExp;

    /** ПополнениеСтатус,0为待审核,1为通过,2为Ошибка */
    @Excel(name = "ПополнениеСтатус,0为待审核,1为通过,2为Ошибка")
    private Integer rechargeState;

    /** Причина операции */
    @Excel(name = "Причина операции")
    private String reason;

    /** ДействияID сервера */
    @Excel(name = "ДействияID сервера")
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
