package com.backend.struct;

public class ShopInfo {

    private Integer id;                 //商品唯一ID

    private Integer itemid;             //道具ID

    private Integer shopid;             //商城id，1元宝/2兑换/3福地积分/4仙盟贡献

    private Integer labelid;            //商城标签

    private Integer level;              //购买需求等级

    private Integer militarylevel = 0;      //购买需求军衔

    private Integer guildlevel;         //购买需求帮会等级

    private Integer guildshoplvlstart;  //购买需求仙盟商店起始等级

    private Integer guildshoplvlend;    //购买需求仙盟商店结束等级

    private Integer worldlvlstart;      //购买需求最低世界等级

    private Integer worldlvlend;        //购买需求结束世界等级

    private Integer isdiscount;         //开通修神锻体后是否打折

    private Integer viplevel;           //购买需求境界等级

    private Integer occupation;         //角色职业限制，-1无限制/0玄剑/1天英/2地藏/3罗刹

    private Integer limittype;          //限购类型，0不限购/1日限够/2周限购/3月限购/4年限购/5终身限购

    private Integer buynum;             //可购买次数

    private Integer currencyid;         //货币ID

    private Integer price;              //打折前价格

    private Integer discountprice;      //打折后价格

    private Integer discount;           //打折数

    private Integer promotion;          //促销标签，0无/1打折/2推荐/3新品/4热卖

    private Integer sort;               //排列优先级

    private String uptime;              //上架时间

    private String downtime;            //下架时间

    private String overdue = "";             //过期日期

    private Integer duration = 0;           //持续时间

    private Integer bind;               //是否绑定

    private Integer refreshcurrency;    //刷新使用货币类型，-1为不能刷新

    private Integer refreshnum;         //刷新货币消耗数量

    private String shoptype;            //商城标签

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
}
