package com.game.recharge.structs;

import java.util.Map;

/**
 * 服务器下单数据
 * Created by cxl on 2021/5/8.
 */
public class ServerOrderData {

    Map<String,String> data;
    private int state;

    private int goodsId;

    private long roleId;

    private String moneyType;


    public Map<String, String> getData() {
        return data;
    }
    public void setData(Map<String, String> data) {
        this.data = data;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }
}
