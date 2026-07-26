package com.gm.project.gmtool.rechargeItem.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Настройки пополнения对象 t_recharge_item
 * 
 * @author gm
 * @date 2021-08-25
 */
public class RechargeItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID пополнения */
    @Excel(name = "ID пополнения")
    private Integer goodsId;

    /** Внутренний ID конфигурации */
    @Excel(name = "Внутренний ID конфигурации")
    private Integer goodsSystemCfgId;

    /** 商品名字描述（主要用于BI后台Данные） */
    @Excel(name = "商品名字描述", readConverterExp = "主=要用于BI后台Данные")
    private String goodsName;

    /** Название канала */
    @Excel(name = "Название канала")
    private String goodsPayChannel;

    /** Платёжный канал（№三方支付） */
    @Excel(name = "Платёжный канал", readConverterExp = "№=三方支付")
    private Integer goodsPayType;

    /** Тип пополнения */
    @Excel(name = "Тип пополнения")
    private Integer goodsType;

    /** Подтип пополнения */
    @Excel(name = "Подтип пополнения")
    private Integer goodsSubtype;

    /** Количество пополнений（当前轮每个挡位对应Пополнение的次数) */
    @Excel(name = "Количество пополнений", readConverterExp = "Количество пополнений（当前轮每个挡位对应Пополнение的次数)")
    private Integer goodsLimit;

    /** ID отображаемой иконки */
    @Excel(name = "ID отображаемой иконки")
    private Integer goodsIcon;

    /** URL изображения товара */
    @Excel(name = "URL изображения товара")
    private String goodsurl;

    /** Пополнение档位对应消耗的真实货币 */
    @Excel(name = "Пополнение档位对应消耗的真实货币")
    private String goodsPrice;

    /** Пополнение计费点 */
    @Excel(name = "Пополнение计费点")
    private String goodsPricePoint;

    /** 界面默认Показывать的货币 例如:THB */
    @Excel(name = "界面默认Показывать的货币 例如:THB")
    private String goodsShowPrice;

    /** Награда за пополнение */
    @Excel(name = "Награда за пополнение")
    private String goodsReward;

    /** Множитель награды */
    @Excel(name = "Множитель награды")
    private String goodsMultiple;

    /** Дополнительная награда */
    @Excel(name = "Дополнительная награда")
    private String goodsExtraReward;

    /** Количество дополнительных наград */
    @Excel(name = "Количество дополнительных наград")
    private Integer goodsExtraRewardLimit;

    /** ДаНет计入到游戏累充活动 */
    @Excel(name = "ДаНет计入到游戏累充活动")
    private Integer isTotalRecharge;

    /** ДаНет增加VIP经验 */
    @Excel(name = "ДаНет增加VIP经验")
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
