package com.gm.project.stat.stat_shop_item.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;

public class ShopItemBean extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name = "商店ID")
    private Integer shopId;
    @Excel(name = "道具id")
    private Integer itemmodelid;

    @Excel(name = "道具id")
    private String itemmodelidShow;

    @Excel(name = "货币类型")
    private Integer moneyType;

    @Excel(name = "货币类型")
    private String moneyTypeShow;

    @Excel(name = "购买用户数")
    private Integer users;
    @Excel(name = "购买角色数")
    private Integer roles;
    @Excel(name = "道具销量")
    private Integer totalnum;
    @Excel(name = "消耗货币数")
    private Integer totalgold;
    @Excel(name = "购买次数")
    private Integer totaltimes;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getItemmodelid() {
        return itemmodelid;
    }

    public void setItemmodelid(Integer itemmodelid) {
        this.itemmodelid = itemmodelid;
    }

    public Integer getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public Integer getRoles() {
        return roles;
    }

    public void setRoles(Integer roles) {
        this.roles = roles;
    }

    public Integer getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(Integer totalnum) {
        this.totalnum = totalnum;
    }

    public Integer getTotalgold() {
        return totalgold;
    }

    public void setTotalgold(Integer totalgold) {
        this.totalgold = totalgold;
    }

    public Integer getTotaltimes() {
        return totaltimes;
    }

    public void setTotaltimes(Integer totaltimes) {
        this.totaltimes = totaltimes;
    }

    public String getItemmodelidShow() {
        return itemmodelidShow;
    }

    public void setItemmodelidShow(String itemmodelidShow) {
        this.itemmodelidShow = itemmodelidShow;
    }

    public String getMoneyTypeShow() {
        return moneyTypeShow;
    }

    public void setMoneyTypeShow(String moneyTypeShow) {
        this.moneyTypeShow = moneyTypeShow;
    }
}
