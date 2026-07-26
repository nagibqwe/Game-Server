package com.gm.project.gmtool.shop.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Магазин对象 shop
 * 
 * @author gm
 * @date 2021-09-23
 */
public class Shop extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品唯一ID */
    private Integer ID;

    /** ID предмета */
    @Excel(name = "ID предмета")
    private Integer itemID;

    /** 所属Магазин */
    @Excel(name = "所属Магазин")
    private Integer shopID;

    /** Магазин标签 */
    @Excel(name = "Магазин标签")
    private String shopType;

    /** ID метки */
    @Excel(name = "ID метки")
    private Integer labelID;

    /** 需求Уровень */
    @Excel(name = "需求Уровень")
    private Integer level;

    /** Класс限制 */
    @Excel(name = "Класс限制")
    private Integer occupation;

    /** 限购Тип */
    @Excel(name = "限购Тип")
    private Integer limitType;

    /** 可购买次数 */
    @Excel(name = "可购买次数")
    private Integer buyNum;

    /** 货币 */
    @Excel(name = "货币")
    private Integer currencyID;

    /** 打折前价格 */
    @Excel(name = "打折前价格")
    private Integer price;

    /** 打折后价格 */
    @Excel(name = "打折后价格")
    private Integer discountPrice;

    /** 打折数 */
    @Excel(name = "打折数")
    private Integer discount;

    /** 促销标签 */
    @Excel(name = "促销标签")
    private Integer promotion;

    /** 排列优先级 */
    @Excel(name = "排列优先级")
    private Integer sort;

    /** Время публикации */
    @Excel(name = "Время публикации")
    private String upTime;

    /** Время снятия */
    @Excel(name = "Время снятия")
    private String downTime;

    /** ДаНет绑定 */
    @Excel(name = "ДаНет绑定")
    private Integer bind;

    /** Обновить使用Тип валюты */
    @Excel(name = "Обновить使用Тип валюты")
    private Integer refreshCurrency;

    /** Обновить货币消耗数量 */
    @Excel(name = "Обновить货币消耗数量")
    private Integer refreshNum;

    /** ДаНет修神锻体打折 */
    @Excel(name = "ДаНет修神锻体打折")
    private Integer isDiscount;

    /** 购买需求军衔 */
    @Excel(name = "购买需求军衔")
    private Integer militaryLevel;

    /** 购买需求帮会Уровень */
    @Excel(name = "购买需求帮会Уровень")
    private Integer guildLevel;

    /** 购买需要的仙盟商店的最低Уровень */
    @Excel(name = "购买需要的仙盟商店的最低Уровень")
    private Integer guildShopLvlStart;

    /** 购买需要的仙盟商店的最高Уровень */
    @Excel(name = "购买需要的仙盟商店的最高Уровень")
    private Integer guildShopLvlEnd;

    /** 购买需求最低世界Уровень */
    @Excel(name = "购买需求最低世界Уровень")
    private Integer worldLvlStart;

    /** 购买需求结束世界Уровень */
    @Excel(name = "购买需求结束世界Уровень")
    private Integer worldLvlEnd;

    /** 购买需求境界Уровень */
    @Excel(name = "购买需求境界Уровень")
    private Integer vipLevel;

    /** 商品的道具过期Время,Год_Месяц_日_时_分_秒,0则取duration */
    @Excel(name = "商品的道具过期Время,Год_Месяц_日_时_分_秒,0则取duration")
    private String overdue;

    /** 持续Время */
    @Excel(name = "持续Время")
    private Integer duration;

    /** 根据购买次数打折 */
    @Excel(name = "根据购买次数打折")
    private String countdiscount;

    /** Время публикации */
    @Excel(name = "Время публикации")
    private Integer openday;

    /** Время снятия */
    @Excel(name = "Время снятия")
    private Integer closeday;

    public void setID(Integer ID)
    {
        this.ID = ID;
    }

    public Integer getID()
    {
        return ID;
    }
    public void setItemID(Integer itemID)
    {
        this.itemID = itemID;
    }

    public Integer getItemID()
    {
        return itemID;
    }
    public void setShopID(Integer shopID)
    {
        this.shopID = shopID;
    }

    public Integer getShopID()
    {
        return shopID;
    }
    public void setShopType(String shopType)
    {
        this.shopType = shopType;
    }

    public String getShopType()
    {
        return shopType;
    }
    public void setLabelID(Integer labelID)
    {
        this.labelID = labelID;
    }

    public Integer getLabelID()
    {
        return labelID;
    }
    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Integer getLevel()
    {
        return level;
    }
    public void setOccupation(Integer occupation)
    {
        this.occupation = occupation;
    }

    public Integer getOccupation()
    {
        return occupation;
    }
    public void setLimitType(Integer limitType)
    {
        this.limitType = limitType;
    }

    public Integer getLimitType()
    {
        return limitType;
    }
    public void setBuyNum(Integer buyNum)
    {
        this.buyNum = buyNum;
    }

    public Integer getBuyNum()
    {
        return buyNum;
    }
    public void setCurrencyID(Integer currencyID)
    {
        this.currencyID = currencyID;
    }

    public Integer getCurrencyID()
    {
        return currencyID;
    }
    public void setPrice(Integer price)
    {
        this.price = price;
    }

    public Integer getPrice()
    {
        return price;
    }
    public void setDiscountPrice(Integer discountPrice)
    {
        this.discountPrice = discountPrice;
    }

    public Integer getDiscountPrice()
    {
        return discountPrice;
    }
    public void setDiscount(Integer discount)
    {
        this.discount = discount;
    }

    public Integer getDiscount()
    {
        return discount;
    }
    public void setPromotion(Integer promotion)
    {
        this.promotion = promotion;
    }

    public Integer getPromotion()
    {
        return promotion;
    }
    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public Integer getSort()
    {
        return sort;
    }
    public void setUpTime(String upTime)
    {
        this.upTime = upTime;
    }

    public String getUpTime()
    {
        return upTime;
    }
    public void setDownTime(String downTime)
    {
        this.downTime = downTime;
    }

    public String getDownTime()
    {
        return downTime;
    }
    public void setBind(Integer bind)
    {
        this.bind = bind;
    }

    public Integer getBind()
    {
        return bind;
    }
    public void setRefreshCurrency(Integer refreshCurrency)
    {
        this.refreshCurrency = refreshCurrency;
    }

    public Integer getRefreshCurrency()
    {
        return refreshCurrency;
    }
    public void setRefreshNum(Integer refreshNum)
    {
        this.refreshNum = refreshNum;
    }

    public Integer getRefreshNum()
    {
        return refreshNum;
    }
    public void setIsDiscount(Integer isDiscount)
    {
        this.isDiscount = isDiscount;
    }

    public Integer getIsDiscount()
    {
        return isDiscount;
    }
    public void setMilitaryLevel(Integer militaryLevel)
    {
        this.militaryLevel = militaryLevel;
    }

    public Integer getMilitaryLevel()
    {
        return militaryLevel;
    }
    public void setGuildLevel(Integer guildLevel)
    {
        this.guildLevel = guildLevel;
    }

    public Integer getGuildLevel()
    {
        return guildLevel;
    }
    public void setGuildShopLvlStart(Integer guildShopLvlStart)
    {
        this.guildShopLvlStart = guildShopLvlStart;
    }

    public Integer getGuildShopLvlStart()
    {
        return guildShopLvlStart;
    }
    public void setGuildShopLvlEnd(Integer guildShopLvlEnd)
    {
        this.guildShopLvlEnd = guildShopLvlEnd;
    }

    public Integer getGuildShopLvlEnd()
    {
        return guildShopLvlEnd;
    }
    public void setWorldLvlStart(Integer worldLvlStart)
    {
        this.worldLvlStart = worldLvlStart;
    }

    public Integer getWorldLvlStart()
    {
        return worldLvlStart;
    }
    public void setWorldLvlEnd(Integer worldLvlEnd)
    {
        this.worldLvlEnd = worldLvlEnd;
    }

    public Integer getWorldLvlEnd()
    {
        return worldLvlEnd;
    }
    public void setVipLevel(Integer vipLevel)
    {
        this.vipLevel = vipLevel;
    }

    public Integer getVipLevel()
    {
        return vipLevel;
    }
    public void setOverdue(String overdue)
    {
        this.overdue = overdue;
    }

    public String getOverdue()
    {
        return overdue;
    }
    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    public Integer getDuration()
    {
        return duration;
    }
    public void setCountdiscount(String countdiscount)
    {
        this.countdiscount = countdiscount;
    }

    public String getCountdiscount()
    {
        return countdiscount;
    }
    public void setOpenday(Integer openday)
    {
        this.openday = openday;
    }

    public Integer getOpenday()
    {
        return openday;
    }
    public void setCloseday(Integer closeday)
    {
        this.closeday = closeday;
    }

    public Integer getCloseday()
    {
        return closeday;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ID", getID())
            .append("itemID", getItemID())
            .append("shopID", getShopID())
            .append("shopType", getShopType())
            .append("labelID", getLabelID())
            .append("level", getLevel())
            .append("occupation", getOccupation())
            .append("limitType", getLimitType())
            .append("buyNum", getBuyNum())
            .append("currencyID", getCurrencyID())
            .append("price", getPrice())
            .append("discountPrice", getDiscountPrice())
            .append("discount", getDiscount())
            .append("promotion", getPromotion())
            .append("sort", getSort())
            .append("upTime", getUpTime())
            .append("downTime", getDownTime())
            .append("bind", getBind())
            .append("refreshCurrency", getRefreshCurrency())
            .append("refreshNum", getRefreshNum())
            .append("isDiscount", getIsDiscount())
            .append("militaryLevel", getMilitaryLevel())
            .append("guildLevel", getGuildLevel())
            .append("guildShopLvlStart", getGuildShopLvlStart())
            .append("guildShopLvlEnd", getGuildShopLvlEnd())
            .append("worldLvlStart", getWorldLvlStart())
            .append("worldLvlEnd", getWorldLvlEnd())
            .append("vipLevel", getVipLevel())
            .append("overdue", getOverdue())
            .append("duration", getDuration())
            .append("countdiscount", getCountdiscount())
            .append("openday", getOpenday())
            .append("closeday", getCloseday())
            .toString();
    }
}
