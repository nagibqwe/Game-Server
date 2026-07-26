package com.gm.project.gmtool.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ShopInfo {

    private Integer id;                 //商品唯一ID

    private Integer itemid;             //ID предмета

    private Integer shopid;             //Магазинid，1元宝/2兑换/3福地积分/4仙盟贡献

    private Integer labelid;            //Магазин标签

    private Integer level;              //购买需求Уровень

    private Integer militarylevel = 0;      //购买需求军衔

    private Integer guildlevel;         //购买需求帮会Уровень

    private Integer guildshoplvlstart;  //购买需求仙盟商店起始Уровень

    private Integer guildshoplvlend;    //购买需求仙盟商店结束Уровень

    private Integer worldlvlstart;      //购买需求最低世界Уровень

    private Integer worldlvlend;        //购买需求结束世界Уровень

    private Integer isdiscount;         //开通修神锻体后ДаНет打折

    private Integer viplevel;           //购买需求境界Уровень

    private Integer occupation;         //Класс персонажа限制，-1无限制/0玄剑/1День英/2地藏/3罗刹

    private Integer limittype;          //限购Тип，0不限购/1日限够/2周限购/3Месяц限购/4Год限购/5终身限购

    private Integer buynum;             //可购买次数

    private Integer currencyid;         //货币ID

    private Integer price;              //打折前价格

    private Integer discountprice;      //打折后价格

    private Integer discount;           //打折数

    private Integer promotion;          //促销标签，0无/1打折/2Рекомендуемый/3新品/4热卖

    private Integer sort;               //排列优先级

    private String uptime;              //Время публикации

    private String downtime;            //Время снятия

    private String overdue = "";             //过期Дата

    private Integer duration = 0;           //持续Время

    private Integer bind;               //ДаНет绑定

    private Integer refreshcurrency;    //Обновить使用Тип валюты，-1为不能Обновить

    private Integer refreshnum;         //Обновить货币消耗数量

    private String shoptype;            //Магазин标签

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }

    public Integer getLabelid() {
        return labelid;
    }

    public void setLabelid(Integer labelid) {
        this.labelid = labelid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getMilitarylevel() {
        return militarylevel;
    }

    public void setMilitarylevel(Integer militarylevel) {
        this.militarylevel = militarylevel;
    }

    public Integer getGuildlevel() {
        return guildlevel;
    }

    public void setGuildlevel(Integer guildlevel) {
        this.guildlevel = guildlevel;
    }

    public Integer getGuildshoplvlstart() {
        return guildshoplvlstart;
    }

    public void setGuildshoplvlstart(Integer guildshoplvlstart) {
        this.guildshoplvlstart = guildshoplvlstart;
    }

    public Integer getGuildshoplvlend() {
        return guildshoplvlend;
    }

    public void setGuildshoplvlend(Integer guildshoplvlend) {
        this.guildshoplvlend = guildshoplvlend;
    }

    public Integer getWorldlvlstart() {
        return worldlvlstart;
    }

    public void setWorldlvlstart(Integer worldlvlstart) {
        this.worldlvlstart = worldlvlstart;
    }

    public Integer getWorldlvlend() {
        return worldlvlend;
    }

    public void setWorldlvlend(Integer worldlvlend) {
        this.worldlvlend = worldlvlend;
    }

    public Integer getIsdiscount() {
        return isdiscount;
    }

    public void setIsdiscount(Integer isdiscount) {
        this.isdiscount = isdiscount;
    }

    public Integer getViplevel() {
        return viplevel;
    }

    public void setViplevel(Integer viplevel) {
        this.viplevel = viplevel;
    }

    public Integer getOccupation() {
        return occupation;
    }

    public void setOccupation(Integer occupation) {
        this.occupation = occupation;
    }

    public Integer getLimittype() {
        return limittype;
    }

    public void setLimittype(Integer limittype) {
        this.limittype = limittype;
    }

    public Integer getBuynum() {
        return buynum;
    }

    public void setBuynum(Integer buynum) {
        this.buynum = buynum;
    }

    public Integer getCurrencyid() {
        return currencyid;
    }

    public void setCurrencyid(Integer currencyid) {
        this.currencyid = currencyid;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscountprice() {
        return discountprice;
    }

    public void setDiscountprice(Integer discountprice) {
        this.discountprice = discountprice;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getPromotion() {
        return promotion;
    }

    public void setPromotion(Integer promotion) {
        this.promotion = promotion;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getDowntime() {
        return downtime;
    }

    public void setDowntime(String downtime) {
        this.downtime = downtime;
    }

    public String getOverdue() {
        return overdue;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getBind() {
        return bind;
    }

    public void setBind(Integer bind) {
        this.bind = bind;
    }

    public Integer getRefreshcurrency() {
        return refreshcurrency;
    }

    public void setRefreshcurrency(Integer refreshcurrency) {
        this.refreshcurrency = refreshcurrency;
    }

    public Integer getRefreshnum() {
        return refreshnum;
    }

    public void setRefreshnum(Integer refreshnum) {
        this.refreshnum = refreshnum;
    }

    public String getShoptype() {
        return shoptype;
    }

    public void setShoptype(String shoptype) {
        this.shoptype = shoptype;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("itemid", getItemid())
                .append("shopid", getShopid())
                .append("labelid", getLabelid())
                .append("level", getLevel())
                .append("militarylevel", getMilitarylevel())
                .append("guildlevel", getGuildlevel())
                .append("guildshoplvlstart", getGuildshoplvlstart())
                .append("guildshoplvlend", getGuildshoplvlend())
                .append("worldlvlstart", getWorldlvlstart())
                .append("worldlvlend", getWorldlvlend())
                .append("isdiscount", getIsdiscount())
                .append("viplevel", getViplevel())
                .append("occupation", getOccupation())
                .append("limittype", getLimittype())
                .append("buynum", getBuynum())
                .append("currencyid", getCurrencyid())
                .append("price", getPrice())
                .append("discountprice", getDiscountprice())
                .append("discount", getDiscount())
                .append("promotion", getPromotion())
                .append("sort", getSort())
                .append("uptime", getUptime())
                .append("downtime", getDowntime())
                .append("overdue", getOverdue())
                .append("duration", getDuration())
                .append("bind", getBind())
                .append("refreshcurrency", getRefreshcurrency())
                .append("refreshnum", getRefreshnum())
                .append("shoptype", getShoptype())
                .toString();
    }
}
