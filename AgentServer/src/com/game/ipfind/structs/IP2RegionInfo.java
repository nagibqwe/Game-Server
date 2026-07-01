package com.game.ipfind.structs;

/**
 * @author gaozhaoguang
 * @desc IP地址转换到区域的数据结构
 * @date Created on 2021/7/29 20:42
 **/
public class IP2RegionInfo {
    //区域ID
    private int id;
    //开始IP地址
    private long sipValue;
    //结束IP地址
    private long eipValue;
    //国家
    private String country;
    //省,市,州
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSipValue() {
        return sipValue;
    }

    public void setSipValue(long sipValue) {
        this.sipValue = sipValue;
    }

    public long getEipValue() {
        return eipValue;
    }

    public void setEipValue(long eipValue) {
        this.eipValue = eipValue;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
