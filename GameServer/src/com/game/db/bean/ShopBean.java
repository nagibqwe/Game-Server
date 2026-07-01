package com.game.db.bean;

import com.data.struct.ReadIntegerArrayEs;
import game.core.db.BaseBean;

import java.util.HashMap;

public class ShopBean extends BaseBean {
    private Integer id;

    private Integer itemid;

    private Integer shopid;

    private Integer labelid;

    private Integer level;

    private Integer militarylevel;

    private Integer guildlevel;

    private Integer guildshoplvlstart;

    private Integer guildshoplvlend;

    private Integer worldlvlstart;

    private Integer worldlvlend;

    private Integer isdiscount;

    private Integer viplevel;

    private Integer occupation;

    private Integer limittype;

    private Integer buynum;

    private Integer currencyid;

    private Integer price;

    private Integer discountprice;

    private Integer discount;

    private Integer promotion;

    private Integer sort;

    private String uptime;

    private String downtime;

    private String overdue;

    private Integer duration;

    private Integer bind;

    private Integer refreshcurrency;

    private Integer refreshnum;

    private String shoptype;

    private transient boolean isMall = true;

    private String countdiscount;

    private ReadIntegerArrayEs countDiscountArrayEs;

    private int openday;

    private int closeday;

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

    public boolean isMall() {
        return isMall;
    }

    public void setMall(boolean mall) {
        isMall = mall;
    }

    public String getCountdiscount() {
        return countdiscount;
    }

    public void setCountdiscount(String countdiscount) {
        this.countdiscount = countdiscount;
    }

    public ReadIntegerArrayEs getCountDiscountArrayEs() {
        if (countDiscountArrayEs == null){
            countDiscountArrayEs = getCountDiscountArrayEs(this.countdiscount);
        }
        return countDiscountArrayEs;
    }
    private ReadIntegerArrayEs getCountDiscountArrayEs(String str){
        ReadIntegerArrayEs countDiscount = new ReadIntegerArrayEs(str,";","_");
        return countDiscount;
    }

    public int getOpenday() {
        return openday;
    }

    public void setOpenday(int openday) {
        this.openday = openday;
    }

    public int getCloseday() {
        return closeday;
    }

    public void setCloseday(int closeday) {
        this.closeday = closeday;
    }

    @Override
    public String toString() {
        return "ShopBean{" +
                "id=" + id +
                ", itemid=" + itemid +
                ", shopid=" + shopid +
                ", labelid=" + labelid +
                ", level=" + level +
                ", militarylevel=" + militarylevel +
                ", guildlevel=" + guildlevel +
                ", guildshoplvlstart=" + guildshoplvlstart +
                ", guildshoplvlend=" + guildshoplvlend +
                ", worldlvlstart=" + worldlvlstart +
                ", worldlvlend=" + worldlvlend +
                ", isdiscount=" + isdiscount +
                ", viplevel=" + viplevel +
                ", occupation=" + occupation +
                ", limittype=" + limittype +
                ", buynum=" + buynum +
                ", currencyid=" + currencyid +
                ", price=" + price +
                ", discountprice=" + discountprice +
                ", discount=" + discount +
                ", promotion=" + promotion +
                ", sort=" + sort +
                ", uptime='" + uptime + '\'' +
                ", downtime='" + downtime + '\'' +
                ", overdue='" + overdue + '\'' +
                ", duration=" + duration +
                ", bind=" + bind +
                ", refreshcurrency=" + refreshcurrency +
                ", refreshnum=" + refreshnum +
                ", shoptype='" + shoptype + '\'' +
                ", isMall=" + isMall +
                ", countDiscount='" + countdiscount + '\'' +
                '}';
    }
}