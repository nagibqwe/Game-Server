package com.gm.project.gamelog.rechargelog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 充值日志对象 log_rechargelog
 * 
 * @author gm
 * @date 2021-09-09
 */
public class Rechargelog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 用户ID */
    @Excel(name = "用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /** 角色ID */
    @Excel(name = "角色ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /** 商品ID */
    @Excel(name = "商品ID")
    private Integer goodsId;

    /** 商品类型 */
    @Excel(name = "商品类型")
    private String goodsType;

    /** 商品额外参数 */
    @Excel(name = "商品额外参数")
    private String goodsExt;

    /** 金额（分） */
    @Excel(name = "金额")
    private Integer totalFee;

    /** 道具ID */
    @Excel(name = "道具ID")
    private Integer itemId;

    /** 货币数 */
    @Excel(name = "货币数")
    private Integer gameMoney;

    /** 额外参数 */
    @Excel(name = "额外参数")
    private String extParam;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 状态码 */
    @Excel(name = "状态码")
    private Integer statusReason;

    /** 角色名 */
    @Excel(name = "角色名")
    private String roleName;

    /** 时间 */
    @Excel(name = "时间")
    private Long time;

    /**  */
    private String platformName;

    /**  */
    private String goodsName;

    /**  */
    private String data;

    /**  */
    private Integer src;

    /**  */
    private String goodsCfg;

    /**  */
    private Integer sid;

    /**  */
    private String moneyType;

    /**  */
    private String sign;

    /**  */
    private Long addTime;

    /**  */
    private String signType;

    /**  */
    private String tradeNo;

    /**  */
    private Long tradeStatus;

    /**  */
    private String notifyId;

    /**  */
    private String notifyTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }
    public void setGoodsId(Integer goodsId)
    {
        this.goodsId = goodsId;
    }

    public Integer getGoodsId()
    {
        return goodsId;
    }
    public void setGoodsType(String goodsType)
    {
        this.goodsType = goodsType;
    }

    public String getGoodsType()
    {
        return goodsType;
    }
    public void setGoodsExt(String goodsExt)
    {
        this.goodsExt = goodsExt;
    }

    public String getGoodsExt()
    {
        return goodsExt;
    }
    public void setTotalFee(Integer totalFee)
    {
        this.totalFee = totalFee;
    }

    public Integer getTotalFee()
    {
        return totalFee;
    }
    public void setItemId(Integer itemId)
    {
        this.itemId = itemId;
    }

    public Integer getItemId()
    {
        return itemId;
    }
    public void setGameMoney(Integer gameMoney)
    {
        this.gameMoney = gameMoney;
    }

    public Integer getGameMoney()
    {
        return gameMoney;
    }
    public void setExtParam(String extParam)
    {
        this.extParam = extParam;
    }

    public String getExtParam()
    {
        return extParam;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setStatusReason(Integer statusReason)
    {
        this.statusReason = statusReason;
    }

    public Integer getStatusReason()
    {
        return statusReason;
    }
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleName()
    {
        return roleName;
    }
    public void setTime(Long time)
    {
        this.time = time;
    }

    public Long getTime()
    {
        return time;
    }
    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformName()
    {
        return platformName;
    }
    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName()
    {
        return goodsName;
    }
    public void setData(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }
    public void setSrc(Integer src)
    {
        this.src = src;
    }

    public Integer getSrc()
    {
        return src;
    }
    public void setGoodsCfg(String goodsCfg)
    {
        this.goodsCfg = goodsCfg;
    }

    public String getGoodsCfg()
    {
        return goodsCfg;
    }
    public void setSid(Integer sid)
    {
        this.sid = sid;
    }

    public Integer getSid()
    {
        return sid;
    }
    public void setMoneyType(String moneyType)
    {
        this.moneyType = moneyType;
    }

    public String getMoneyType()
    {
        return moneyType;
    }
    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getSign()
    {
        return sign;
    }
    public void setAddTime(Long addTime)
    {
        this.addTime = addTime;
    }

    public Long getAddTime()
    {
        return addTime;
    }
    public void setSignType(String signType)
    {
        this.signType = signType;
    }

    public String getSignType()
    {
        return signType;
    }
    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }

    public String getTradeNo()
    {
        return tradeNo;
    }
    public void setTradeStatus(Long tradeStatus)
    {
        this.tradeStatus = tradeStatus;
    }

    public Long getTradeStatus()
    {
        return tradeStatus;
    }
    public void setNotifyId(String notifyId)
    {
        this.notifyId = notifyId;
    }

    public String getNotifyId()
    {
        return notifyId;
    }
    public void setNotifyTime(String notifyTime)
    {
        this.notifyTime = notifyTime;
    }

    public String getNotifyTime()
    {
        return notifyTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("roleId", getRoleId())
            .append("goodsId", getGoodsId())
            .append("goodsType", getGoodsType())
            .append("goodsExt", getGoodsExt())
            .append("totalFee", getTotalFee())
            .append("itemId", getItemId())
            .append("gameMoney", getGameMoney())
            .append("extParam", getExtParam())
            .append("status", getStatus())
            .append("statusReason", getStatusReason())
            .append("roleName", getRoleName())
            .append("time", getTime())
            .append("platformName", getPlatformName())
            .append("goodsName", getGoodsName())
            .append("data", getData())
            .append("src", getSrc())
            .append("goodsCfg", getGoodsCfg())
            .append("sid", getSid())
            .append("moneyType", getMoneyType())
            .append("sign", getSign())
            .append("addTime", getAddTime())
            .append("signType", getSignType())
            .append("tradeNo", getTradeNo())
            .append("tradeStatus", getTradeStatus())
            .append("notifyId", getNotifyId())
            .append("notifyTime", getNotifyTime())
            .toString();
    }
}
