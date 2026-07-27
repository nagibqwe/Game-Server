package com.gm.project.gmtool.rechargeItem.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 充值配置对象 t_recharge_item
 * 
 * @author gm
 * @date 2021-08-25
 */
public class RechargeItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 充值ID */
    @Excel(name = "充值ID")
    private Integer goodsId;

    /** 游戏内部配置ID */
    @Excel(name = "游戏内部配置ID")
    private Integer goodsSystemCfgId;

    /** 商品名字描述（主要用于BI后台数据） */
    @Excel(name = "商品名字描述", readConverterExp = "主=要用于BI后台数据")
    private String goodsName;

    /** 渠道名称 */
    @Excel(name = "渠道名称")
    private String goodsPayChannel;

    /** 支付渠道（第三方支付） */
    @Excel(name = "支付渠道", readConverterExp = "第=三方支付")
    private Integer goodsPayType;

    /** 充值类型 */
    @Excel(name = "充值类型")
    private Integer goodsType;

    /** 充值子类型 */
    @Excel(name = "充值子类型")
    private Integer goodsSubtype;

    /** 充值次数（当前轮每个挡位对应充值的次数) */
    @Excel(name = "充值次数", readConverterExp = "充值次数（当前轮每个挡位对应充值的次数)")
    private Integer goodsLimit;

    /** 显示的图标的ID */
    @Excel(name = "显示的图标的ID")
    private Integer goodsIcon;

    /** 商品图片地址 */
    @Excel(name = "商品图片地址")
    private String goodsurl;

    /** 充值档位对应消耗的真实货币 */
    @Excel(name = "充值档位对应消耗的真实货币")
    private String goodsPrice;

    /** 充值计费点 */
    @Excel(name = "充值计费点")
    private String goodsPricePoint;

    /** 界面默认显示的货币 例如:THB */
    @Excel(name = "界面默认显示的货币 例如:THB")
    private String goodsShowPrice;

    /** 充值奖励 */
    @Excel(name = "充值奖励")
    private String goodsReward;

    /** 充值奖励倍数 */
    @Excel(name = "充值奖励倍数")
    private String goodsMultiple;

    /** 额外奖励 */
    @Excel(name = "额外奖励")
    private String goodsExtraReward;

    /** 额外奖励次数 */
    @Excel(name = "额外奖励次数")
    private Integer goodsExtraRewardLimit;

    /** 是否计入到游戏累充活动 */
    @Excel(name = "是否计入到游戏累充活动")
    private Integer isTotalRecharge;

    /** 是否增加VIP经验 */
    @Excel(name = "是否增加VIP经验")
    private Integer totalVipPower;

    public void setGoodsId(Integer goodsId)
    {
        this.goodsId = goodsId;
    }

    public Integer getGoodsId()
    {
        return goodsId;
    }
    public void setGoodsSystemCfgId(Integer goodsSystemCfgId)
    {
        this.goodsSystemCfgId = goodsSystemCfgId;
    }

    public Integer getGoodsSystemCfgId()
    {
        return goodsSystemCfgId;
    }
    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName()
    {
        return goodsName;
    }
    public void setGoodsPayChannel(String goodsPayChannel)
    {
        this.goodsPayChannel = goodsPayChannel;
    }

    public String getGoodsPayChannel()
    {
        return goodsPayChannel;
    }
    public void setGoodsPayType(Integer goodsPayType)
    {
        this.goodsPayType = goodsPayType;
    }

    public Integer getGoodsPayType()
    {
        return goodsPayType;
    }
    public void setGoodsType(Integer goodsType)
    {
        this.goodsType = goodsType;
    }

    public Integer getGoodsType()
    {
        return goodsType;
    }
    public void setGoodsSubtype(Integer goodsSubtype)
    {
        this.goodsSubtype = goodsSubtype;
    }

    public Integer getGoodsSubtype()
    {
        return goodsSubtype;
    }
    public void setGoodsLimit(Integer goodsLimit)
    {
        this.goodsLimit = goodsLimit;
    }

    public Integer getGoodsLimit()
    {
        return goodsLimit;
    }
    public void setGoodsIcon(Integer goodsIcon)
    {
        this.goodsIcon = goodsIcon;
    }

    public Integer getGoodsIcon()
    {
        return goodsIcon;
    }

    public String getGoodsurl() {
        return goodsurl;
    }

    public void setGoodsurl(String goodsurl) {
        this.goodsurl = goodsurl;
    }

    public void setGoodsPrice(String goodsPrice)
    {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsPrice()
    {
        return goodsPrice;
    }
    public void setGoodsPricePoint(String goodsPricePoint)
    {
        this.goodsPricePoint = goodsPricePoint;
    }

    public String getGoodsPricePoint()
    {
        return goodsPricePoint;
    }
    public void setGoodsShowPrice(String goodsShowPrice)
    {
        this.goodsShowPrice = goodsShowPrice;
    }

    public String getGoodsShowPrice()
    {
        return goodsShowPrice;
    }
    public void setGoodsReward(String goodsReward)
    {
        this.goodsReward = goodsReward;
    }

    public String getGoodsReward()
    {
        return goodsReward;
    }
    public void setGoodsMultiple(String goodsMultiple)
    {
        this.goodsMultiple = goodsMultiple;
    }

    public String getGoodsMultiple()
    {
        return goodsMultiple;
    }
    public void setGoodsExtraReward(String goodsExtraReward)
    {
        this.goodsExtraReward = goodsExtraReward;
    }

    public String getGoodsExtraReward()
    {
        return goodsExtraReward;
    }
    public void setGoodsExtraRewardLimit(Integer goodsExtraRewardLimit)
    {
        this.goodsExtraRewardLimit = goodsExtraRewardLimit;
    }

    public Integer getGoodsExtraRewardLimit()
    {
        return goodsExtraRewardLimit;
    }
    public void setIsTotalRecharge(Integer isTotalRecharge)
    {
        this.isTotalRecharge = isTotalRecharge;
    }

    public Integer getIsTotalRecharge()
    {
        return isTotalRecharge;
    }
    public void setTotalVipPower(Integer totalVipPower)
    {
        this.totalVipPower = totalVipPower;
    }

    public Integer getTotalVipPower()
    {
        return totalVipPower;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("goodsId", getGoodsId())
            .append("goodsSystemCfgId", getGoodsSystemCfgId())
            .append("goodsName", getGoodsName())
            .append("goodsPayChannel", getGoodsPayChannel())
            .append("goodsPayType", getGoodsPayType())
            .append("goodsType", getGoodsType())
            .append("goodsSubtype", getGoodsSubtype())
            .append("goodsLimit", getGoodsLimit())
            .append("goodsIcon", getGoodsIcon())
            .append("goodsurl", getGoodsurl())
            .append("goodsPrice", getGoodsPrice())
            .append("goodsPricePoint", getGoodsPricePoint())
            .append("goodsShowPrice", getGoodsShowPrice())
            .append("goodsReward", getGoodsReward())
            .append("goodsMultiple", getGoodsMultiple())
            .append("goodsExtraReward", getGoodsExtraReward())
            .append("goodsExtraRewardLimit", getGoodsExtraRewardLimit())
            .append("isTotalRecharge", getIsTotalRecharge())
            .append("totalVipPower", getTotalVipPower())
            .toString();
    }
}
